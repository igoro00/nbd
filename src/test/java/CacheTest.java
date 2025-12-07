import org.example.managers.MovieManager;
import org.example.managers.ClientManager;
import org.example.managers.HallManager;
import org.example.model.Client;
import org.example.model.Address;
import org.example.model.Hall;
import org.example.repositories.ClientRepository;
import org.example.repositories.HallRepository;
import org.example.repositories.cache.ClientRepositoryCacheDecorator;
import org.example.repositories.cache.HallRepositoryCacheDecorator;
import org.example.managers.RedisManager;
import org.example.model.Movie;
import org.example.repositories.MovieRepository;
import org.example.repositories.cache.MovieRepositoryCacheDecorator;
import org.junit.jupiter.api.*;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CacheTest {

    private MovieRepository movieRepository;
    private ClientRepository clientRepository;
    private HallRepository hallRepository;

    private MovieRepositoryCacheDecorator movieCacheDecorator;
    private ClientRepositoryCacheDecorator clientCacheDecorator;
    private HallRepositoryCacheDecorator hallCacheDecorator;

    private MovieManager movieManager;
    private ClientManager clientManager;
    private HallManager hallManager;

    @BeforeEach
    void setUp() {
        RedisManager redisManager = new RedisManager();

        movieRepository = new MovieRepository();
        clientRepository = new ClientRepository();
        hallRepository = new HallRepository();

        movieCacheDecorator = new MovieRepositoryCacheDecorator(movieRepository, redisManager);
        clientCacheDecorator = new ClientRepositoryCacheDecorator(clientRepository, redisManager);
        hallCacheDecorator = new HallRepositoryCacheDecorator(hallRepository, redisManager);

        movieManager = new MovieManager(movieCacheDecorator);
        clientManager = new ClientManager(clientCacheDecorator);
        hallManager = new HallManager(hallCacheDecorator);

        movieRepository.dropDatabase();
        clientRepository.dropDatabase();
        hallRepository.dropDatabase();

        try (Jedis jedis = redisManager.getResource()) {
            jedis.flushDB();
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        movieRepository.close();
        clientRepository.close();
        hallRepository.close();
    }

    @Test
    void addMovieAndCheckCache() {
        Duration duration = Duration.ofMinutes(120);
        movieManager.createMovie(
                "Inception",
                duration,
                "Sci-Fi",
                10.0,
                "Steven",
                "Spielberg"
        );
        List<Movie> movies1 = movieManager.getAll();
        assertFalse(movies1.isEmpty());
        assertTrue(movies1.stream().anyMatch(m -> "Inception".equals(m.getTitle())));
        List<Movie> movies2 = movieManager.getAll();
        assertEquals(movies1.size(), movies2.size());
        assertTrue(movies2.stream().anyMatch(m -> "Inception".equals(m.getTitle())));
    }

    @Test
    void invalidateAllOnAdd() {
        movieManager.createMovie("Inception", Duration.ofMinutes(100), "Sci-Fi", 10.0, "Steven", "Spielberg");
        List<Movie> firstLoad = movieManager.getAll();
        movieManager.createMovie("Inception", Duration.ofMinutes(120), "Sci-Fi", 10.0, "Steven", "Spielberg");
        List<Movie> secondLoad = movieManager.getAll();
        assertEquals(1, firstLoad.size(), "Cache listy nie został unieważniony!");
        assertEquals(2, secondLoad.size(), "Cache listy nie został unieważniony!");
    }

    @Test
    void invalidateCountOnAdd() {
        movieManager.createMovie("Inception", Duration.ofMinutes(100), "Sci-Fi", 10.0, "Steven", "Spielberg");
        long count1 = movieCacheDecorator.countAll();
        movieManager.createMovie("Inception", Duration.ofMinutes(120), "Sci-Fi", 10.0, "Steven", "Spielberg");
        long count2 = movieCacheDecorator.countAll();
        assertEquals(count1 + 1, count2, "Cache count nie został unieważniony!");
    }

    @Test
    void addClientAndCheckCache() {
        Address address = new Address("Łódź", "90-105", "Piotrkowska", "69/8");
        clientManager.registerClient("Martin", "Smith", "martin.smith@example.com",
                new java.util.GregorianCalendar(1978, java.util.Calendar.SEPTEMBER, 29).getTime(),
                address);

        List<Client> clients1 = clientManager.getAll();
        assertFalse(clients1.isEmpty());
        assertTrue(clients1.stream().anyMatch(c -> "Martin".equals(c.getFirstName()) && "Smith".equals(c.getLastName())));

        List<Client> clients2 = clientManager.getAll();
        assertEquals(clients1.size(), clients2.size());
        assertTrue(clients2.stream().anyMatch(c -> "Martin".equals(c.getFirstName()) && "Smith".equals(c.getLastName())));
    }

    @Test
    void clientCacheInvalidationOnAdd() {
        Address address = new Address("Warsaw", "00-001", "Marszałkowska", "1");
        clientManager.registerClient("Alice", "Johnson", "alice@example.com",
                new java.util.GregorianCalendar(1990, java.util.Calendar.JANUARY, 5).getTime(),
                address);

        long countBefore = clientCacheDecorator.countAll();
        clientManager.registerClient("Bob", "Brown", "bob@example.com",
                new java.util.GregorianCalendar(1985, java.util.Calendar.JUNE, 10).getTime(),
                new Address("Krakow", "30-001", "Floriańska", "2"));

        long countAfter = clientCacheDecorator.countAll();

        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    void addHallAndCheckCache() {
        hallManager.createHall("Main Hall", 20, 15);

        List<Hall> halls1 = hallManager.getAll();
        assertFalse(halls1.isEmpty());
        assertTrue(halls1.stream().anyMatch(h -> "Main Hall".equals(h.getName())));

        List<Hall> halls2 = hallManager.getAll();
        assertEquals(halls1.size(), halls2.size());
        assertTrue(halls2.stream().anyMatch(h -> "Main Hall".equals(h.getName())));
    }

    @Test
    void hallCacheInvalidationOnAdd() {
        long countBefore = hallCacheDecorator.countAll();
        hallManager.createHall("VIP Hall", 10, 10);
        long countAfter = hallCacheDecorator.countAll();
        assertEquals(countBefore + 1, countAfter);
    }
}
