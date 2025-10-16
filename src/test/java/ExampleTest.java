import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.Repository;
import org.example.managers.ClientManager;
import org.example.managers.DirectorManager;
import org.example.managers.MovieManager;
import org.example.model.Movie;
import org.example.model.persons.Client;
import org.example.model.persons.Director;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Testcontainers
class ExampleTest {
    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new
            PostgreSQLContainer<>(DockerImageName.parse("postgres:17"))
            .withDatabaseName("nbddb")
            .withUsername("nbd")
            .withPassword("nbdpassword")
            .withExposedPorts(5432);

    private Repository<Client> clientRepository;
    private ClientManager clientManager;
    private Repository<Movie> movieRepository;
    private MovieManager movieManager;
    private Repository<Director> directorRepository;
    private DirectorManager directorManager;

    @BeforeEach
    public void connect(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("POSTGRES_CINEMA_PU");
        EntityManager em = emf.createEntityManager();
        this.clientRepository = new Repository<Client>(Client.class, em);
        this.clientManager = new ClientManager(clientRepository);
        this.movieRepository = new Repository<Movie>(Movie.class, em);
        this.movieManager = new MovieManager(movieRepository);
        this.directorRepository = new Repository<Director>(Director.class, em);
        this.directorManager = new DirectorManager(directorRepository);
    }

    @Test
    void isRepository(){
        Assertions.assertNotNull(clientRepository);
        Assertions.assertNotNull(clientManager);
    }

    @Test
    void createUserTest() {
        Date date = new GregorianCalendar(1990, 0, 1).getTime();
        Client client = clientManager.registerClient(
                 "John",
                 "Doe",
                 date,
                 "example@example.com");
        Assertions.assertEquals("John", client.getFirstName());
        Assertions.assertEquals("Doe", client.getLastName());
        Assertions.assertEquals(date, client.getDateOfBirth());
        Assertions.assertEquals("example@example.com", client.getEmail());

        List<Client> ClientList = clientManager.getAll();
        Assertions.assertEquals(1, ClientList.size());
        Assertions.assertEquals("John", ClientList.getFirst().getFirstName());
        Assertions.assertEquals("Doe", ClientList.getFirst().getLastName());
        Assertions.assertEquals(date, ClientList.getFirst().getDateOfBirth());
        Assertions.assertEquals("example@example.com", ClientList.getFirst().getEmail());
    }

    @Test
    void createDirectorTest() {
        Director director = directorManager.registerDirector("Steven", "Spielberg");
        Assertions.assertEquals("Steven", director.getFirstName());
        Assertions.assertEquals("Spielberg", director.getLastName());
        List<Director> directorList = directorManager.getAll();
        Assertions.assertEquals(1, directorList.size());
        Assertions.assertEquals("Steven", directorList.getFirst().getFirstName());
        Assertions.assertEquals("Spielberg", directorList.getFirst().getLastName());
    }

    @Test
    void createMovieTest(){
        Director director = directorManager.registerDirector("Steven", "Spielberg");
        Date date = new GregorianCalendar(1990, 0, 1).getTime();
        Duration duration = Duration.ofMinutes(120);
        Movie movie = movieManager.createMovie(
                "Inception",
                date,
                duration,
                "Sci-Fi",
                10.0,
                director
                );
        Assertions.assertEquals("Inception", movie.getTitle());
        Assertions.assertEquals(date, movie.getStartShowDate());
        Assertions.assertEquals(duration, movie.getTimeDuration());
        Assertions.assertEquals("Sci-Fi", movie.getCategory());
        Assertions.assertEquals(10.0, movie.getBasicPrice());
        Assertions.assertEquals(director, movie.getDirector());
        List<Movie> movieList = movieRepository.findAll();
        Assertions.assertEquals(1, movieList.size());
        Assertions.assertEquals("Inception", movieList.getFirst().getTitle());
        Assertions.assertEquals(date, movieList.getFirst().getStartShowDate());
        Assertions.assertEquals(duration, movieList.getFirst().getTimeDuration());
        Assertions.assertEquals("Sci-Fi", movieList.getFirst().getCategory());
        Assertions.assertEquals(10.0, movieList.getFirst().getBasicPrice());
        Assertions.assertEquals(director, movieList.getFirst().getDirector());

    }

}
