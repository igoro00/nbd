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
    private RedisManager redisManager;
    private MovieRepositoryCacheDecorator movieCacheDecorator;
    private ClientRepositoryCacheDecorator clientCacheDecorator;
    private HallRepositoryCacheDecorator hallCacheDecorator;
    private MovieManager movieManager;
    private ClientManager clientManager;
    private HallManager hallManager;

    @BeforeEach
    void setUp() throws Exception {
        movieRepository = new MovieRepository();
        clientRepository = new ClientRepository();
        hallRepository = new HallRepository();
        redisManager = new RedisManager();
        movieCacheDecorator = new MovieRepositoryCacheDecorator(movieRepository, redisManager);
        clientCacheDecorator = new ClientRepositoryCacheDecorator(clientRepository, redisManager);
        hallCacheDecorator = new HallRepositoryCacheDecorator(hallRepository, redisManager);
        movieManager = new MovieManager(movieRepository);
        clientManager = new ClientManager(clientRepository);
        hallManager = new HallManager(hallRepository);
        clientRepository.dropDatabase();
        try (Jedis jedis = redisManager.getResource()) {
            jedis.flushDB();
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        clientRepository.close();
        movieRepository.close();
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
        List<Movie> movies1 = movieCacheDecorator.findAll();
        assertFalse(movies1.isEmpty());
        assertTrue(movies1.stream().anyMatch(m -> "Inception".equals(m.getTitle())));
        List<Movie> movies2 = movieCacheDecorator.findAll();
        assertEquals(movies1.size(), movies2.size());
        assertTrue(movies2.stream().anyMatch(m -> "Inception".equals(m.getTitle())));
    }

        @Test
        void addClientAndCheckCache() {
        Address address = new Address(
            "Łódź",
            "90-105",
            "Piotrkowska",
            "69/8"
        );
        clientManager.registerClient(
            "Martin",
            "Smith",
            "martin.smith@example.com",
            new java.util.GregorianCalendar(1978, java.util.Calendar.SEPTEMBER, 29).getTime(),
            address
        );
        List<Client> clients1 = clientCacheDecorator.findAll();
        assertFalse(clients1.isEmpty());
        assertTrue(clients1.stream().anyMatch(c -> "Martin".equals(c.getFirstName()) && "Smith".equals(c.getLastName())));
        List<Client> clients2 = clientCacheDecorator.findAll();
        assertEquals(clients1.size(), clients2.size());
        assertTrue(clients2.stream().anyMatch(c -> "Martin".equals(c.getFirstName()) && "Smith".equals(c.getLastName())));
        }

    @Test
    void addHallAndCheckCache() {
        hallManager.createHall("Main Hall", 20, 15);
        List<Hall> halls1 = hallCacheDecorator.findAll();
        assertFalse(halls1.isEmpty());
        assertTrue(halls1.stream().anyMatch(h -> "Main Hall".equals(h.getName())));
        List<Hall> halls2 = hallCacheDecorator.findAll();
        assertEquals(halls1.size(), halls2.size());
        assertTrue(halls2.stream().anyMatch(h -> "Main Hall".equals(h.getName())));
    }
}
