import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.managers.ClientManager;
import org.example.managers.DirectorManager;
import org.example.managers.MovieManager;
import org.example.model.Address;
import org.example.model.Movie;
import org.example.model.Screening;
import org.example.model.Ticket;
import org.example.model.persons.Client;
import org.example.model.persons.Director;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Testcontainers
class ExampleTest {
    private ClientManager clientManager;
    private MovieManager movieManager;
    private DirectorManager directorManager;

    @BeforeEach
    public void setUp(){
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
        Assertions.assertEquals(duration, movie.getTimeDuration());
        Assertions.assertEquals("Sci-Fi", movie.getCategory());
        Assertions.assertEquals(10.0, movie.getBasicPrice());
        Assertions.assertEquals(director, movie.getDirector());
        List<Movie> movieList = movieManager.getAll();
        Assertions.assertEquals(1, movieList.size());
        Assertions.assertEquals("Inception", movieList.getFirst().getTitle());
        Assertions.assertEquals(duration, movieList.getFirst().getTimeDuration());
        Assertions.assertEquals("Sci-Fi", movieList.getFirst().getCategory());
        Assertions.assertEquals(10.0, movieList.getFirst().getBasicPrice());
        Assertions.assertEquals(director, movieList.getFirst().getDirector());
    }

    @Test
    void ticketTest() {
        Director director = directorManager.registerDirector("Steven", "Spielberg");
        Movie movie = movieManager.createMovie("Jurassic Park", new Date(), Duration.ofMinutes(127), "Adventure", 15.0, director);
        Screening screening = movieManager.createScreening(movie, null, new Date());
        Ticket ticket = new Ticket();
    }
}
