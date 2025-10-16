import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.Repository;
import org.example.managers.ClientManager;
import org.example.managers.DirectorManager;
import org.example.managers.MovieManager;
import org.example.model.Address;
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
import java.util.Calendar;
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

    private ClientManager clientManager;
    private Repository<Movie> movieRepository;
    private MovieManager movieManager;
    private Repository<Director> directorRepository;
    private DirectorManager directorManager;

    @BeforeEach
    public void connect(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("POSTGRES_CINEMA_PU");
        EntityManager em = emf.createEntityManager();

        this.clientManager = new ClientManager(em);
        this.movieManager = new MovieManager(em);
        this.directorManager = new DirectorManager(em);
    }

    @Test
    void isRepository(){
        Assertions.assertNotNull(clientManager);
    }

    @Test
    void createClientTest() {
        Date date = new GregorianCalendar(1978, Calendar.SEPTEMBER, 29).getTime();

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
            date,
            address
        );

        List<Client> clientList = clientManager.getAll();
        Client client = clientList.getFirst();
        Assertions.assertEquals(1, clientList.size());

        Assertions.assertEquals("Martin", client.getFirstName());
        Assertions.assertEquals("Smith", client.getLastName());
        Assertions.assertEquals(date, client.getDateOfBirth());
        Assertions.assertEquals("martin.smith@example.com", client.getEmail());

        Assertions.assertEquals("Łódź", client.getAddress().getCity());
        Assertions.assertEquals("90-105", client.getAddress().getZipCode());
        Assertions.assertEquals("Piotrkowska", client.getAddress().getStreet());
        Assertions.assertEquals("69/8", client.getAddress().getNumber());
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
        List<Movie> movieList = movieManager.getAll();
        Assertions.assertEquals(1, movieList.size());
        Assertions.assertEquals("Inception", movieList.getFirst().getTitle());
        Assertions.assertEquals(date, movieList.getFirst().getStartShowDate());
        Assertions.assertEquals(duration, movieList.getFirst().getTimeDuration());
        Assertions.assertEquals("Sci-Fi", movieList.getFirst().getCategory());
        Assertions.assertEquals(10.0, movieList.getFirst().getBasicPrice());
        Assertions.assertEquals(director, movieList.getFirst().getDirector());
    }

}
