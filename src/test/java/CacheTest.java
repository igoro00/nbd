import org.bson.types.ObjectId;
import org.example.managers.MovieManager;
import org.example.managers.ClientManager;
import org.example.managers.HallManager;
import org.example.model.Client;
import org.example.model.Address;
import org.example.model.Hall;
import org.example.model.Movie;
import org.example.repositories.ClientRepository;
import org.example.repositories.HallRepository;
import org.example.repositories.MovieRepository;
import org.example.repositories.cache.ClientRepositoryCacheDecorator;
import org.example.repositories.cache.HallRepositoryCacheDecorator;
import org.example.repositories.cache.MovieRepositoryCacheDecorator;
import org.example.managers.RedisManager;
import org.junit.jupiter.api.*;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class CacheTest {

    private MovieRepository movieRepository;
    private ClientRepository clientRepository;
    private HallRepository hallRepository;

    private MovieRepositoryCacheDecorator movieCacheDecorator;

    private MovieManager movieManager;
    private ClientManager clientManager;
    private HallManager hallManager;

    private AtomicInteger movieMongoCalls;
    private AtomicInteger clientMongoCalls;
    private AtomicInteger hallMongoCalls;


    private static class CountingMovieRepository extends MovieRepository {
        private final MovieRepository base;
        private final AtomicInteger counter;

        public CountingMovieRepository(AtomicInteger counter) {
            this.base = new MovieRepository();
            this.counter = counter;
        }

        @Override
        public Movie findById(ObjectId id) {
            counter.incrementAndGet();
            return base.findById(id);
        }

        @Override
        public List<Movie> findAll() {
            counter.incrementAndGet();
            return base.findAll();
        }

        @Override
        public long countAll() {
            counter.incrementAndGet();
            return base.countAll();
        }

        @Override
        public Movie add(Movie movie) { return base.add(movie); }

        @Override
        public void dropDatabase() { base.dropDatabase(); }
    }

    private static class CountingClientRepository extends ClientRepository {
        private final ClientRepository base;
        private final AtomicInteger counter;

        public CountingClientRepository(AtomicInteger counter) {
            this.base = new ClientRepository();
            this.counter = counter;
        }

        @Override
        public Client findById(ObjectId id) {
            counter.incrementAndGet();
            return base.findById(id);
        }

        @Override
        public List<Client> findAll() {
            counter.incrementAndGet();
            return base.findAll();
        }

        @Override
        public void dropDatabase() { base.dropDatabase(); }

        @Override
        public Client add(Client client) { return base.add(client); }
    }

    private static class CountingHallRepository extends HallRepository {
        private final HallRepository base;
        private final AtomicInteger counter;

        public CountingHallRepository(AtomicInteger counter) {
            this.base = new HallRepository();
            this.counter = counter;
        }

        @Override
        public Hall findById(ObjectId id) {
            counter.incrementAndGet();
            return base.findById(id);
        }

        @Override
        public List<Hall> findAll() {
            counter.incrementAndGet();
            return base.findAll();
        }

        @Override
        public void dropDatabase() { base.dropDatabase(); }

        @Override
        public Hall add(Hall hall) { return base.add(hall); }
    }

    @BeforeEach
    void setUp() {
        RedisManager redisManager = new RedisManager();

        movieMongoCalls = new AtomicInteger(0);
        clientMongoCalls = new AtomicInteger(0);
        hallMongoCalls = new AtomicInteger(0);

        movieRepository = new CountingMovieRepository(movieMongoCalls);
        clientRepository = new CountingClientRepository(clientMongoCalls);
        hallRepository = new CountingHallRepository(hallMongoCalls);

        movieCacheDecorator = new MovieRepositoryCacheDecorator(movieRepository, redisManager);
        ClientRepositoryCacheDecorator clientCacheDecorator = new ClientRepositoryCacheDecorator(clientRepository, redisManager);
        HallRepositoryCacheDecorator hallCacheDecorator = new HallRepositoryCacheDecorator(hallRepository, redisManager);

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
        movieManager.createMovie("Inception", Duration.ofMinutes(120), "Sci-Fi", 10.0, "Steven", "Spielberg");
        movieManager.getAll();
        assertEquals(1, movieMongoCalls.get(), "Pierwszy odczyt powinien trafić do Mongo");
        movieManager.getAll();
        assertEquals(1, movieMongoCalls.get(), "Drugi odczyt powinien być z cache");
    }

    @Test
    void invalidateAllOnAdd() {
        movieManager.createMovie("Inception", Duration.ofMinutes(100), "Sci-Fi", 10.0, "S", "S");
        movieManager.getAll();
        movieMongoCalls.set(0);
        movieManager.createMovie("Inception2", Duration.ofMinutes(120), "Sci-Fi", 10.0, "S", "S");
        movieManager.getAll();
        assertEquals(1, movieMongoCalls.get(), "Po dodaniu lista powinna się przebudować z Mongo");
    }

    @Test
    void invalidateCountOnAdd() {
        movieManager.createMovie("Inception", Duration.ofMinutes(100), "Sci-Fi", 10.0, "S", "S");
        movieCacheDecorator.countAll();
        assertEquals(1, movieMongoCalls.get());
        movieManager.createMovie("Interstellar", Duration.ofMinutes(169), "Sci-Fi", 10.0, "S", "S");
        movieCacheDecorator.countAll();
        assertEquals(2, movieMongoCalls.get());
    }

    @Test
    void addClientAndCheckCache() {
        Address address = new Address(
                "Łódź",
                "90-105",
                "Piotrkowska",
                "69/8"
        );
        clientManager.registerClient("Martin", "Smith", "martin@example.com",
                new java.util.Date(), address);
        clientManager.getAll();
        assertEquals(1, clientMongoCalls.get());
        clientManager.getAll();
        assertEquals(1, clientMongoCalls.get());
        clientManager.registerClient("Martin2", "Smith2", "martin2@example.com",
                new java.util.Date(), address);
        clientManager.getAll();
        assertEquals(2, clientMongoCalls.get());

    }

    @Test
    void addHallAndCheckCache() {
        hallManager.createHall("Main Hall", 20, 15);
        hallManager.getAll();
        assertEquals(1, hallMongoCalls.get());
        hallManager.getAll();
        assertEquals(1, hallMongoCalls.get());
        hallManager.createHall("Main Hall2", 22, 16);
        hallManager.getAll();
        assertEquals(2, hallMongoCalls.get());
    }
}
