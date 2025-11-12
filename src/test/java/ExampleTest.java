import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.managers.*;
import org.example.model.*;
import org.example.model.Client;
import org.example.model.Director;
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
    private HallManager hallManager;
    private TicketManager ticketManager;
    private ScreeningManager screeningManager;

    @BeforeEach
    public void setUp(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("POSTGRES_CINEMA_PU");
        EntityManager em = emf.createEntityManager();
        this.clientManager = new ClientManager(em);
        this.movieManager = new MovieManager(em);
        this.screeningManager = new ScreeningManager(em);
        this.directorManager = new DirectorManager(em);
        this.hallManager = new HallManager(em);
        this.ticketManager = new TicketManager(em);
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
        Duration duration = Duration.ofMinutes(120);
        Movie movie = movieManager.createMovie(
            "Inception",
            duration,
            "Sci-Fi",
            10.0,
            director
            );
        Assertions.assertEquals("Inception", movie.getTitle());
        Assertions.assertEquals(duration, movie.getDuration());
        Assertions.assertEquals("Sci-Fi", movie.getCategory());
        Assertions.assertEquals(10.0, movie.getBasicPrice());
        Assertions.assertEquals(director, movie.getDirector());
        List<Movie> movieList = movieManager.getAll();
        Assertions.assertEquals(1, movieList.size());
        Assertions.assertEquals("Inception", movieList.getFirst().getTitle());
        Assertions.assertEquals(duration, movieList.getFirst().getDuration());
        Assertions.assertEquals("Sci-Fi", movieList.getFirst().getCategory());
        Assertions.assertEquals(10.0, movieList.getFirst().getBasicPrice());
        Assertions.assertEquals(director, movieList.getFirst().getDirector());
    }

    @Test
    void screeningTest() {
        Director director = directorManager.registerDirector("Steven", "Spielberg");
        Movie movie = movieManager.createMovie("Jurassic Park", Duration.ofMinutes(120), "Adventure", 15.0, director);
        Hall hall = hallManager.createHall("sala",10, 10);
        Hall hall2 = hallManager.createHall("sala2",5,5);
        Date date = new GregorianCalendar(2025, Calendar.MARCH, 1, 15, 30).getTime();

        Assertions.assertEquals(0, screeningManager.getScreeningCount());

        Assertions.assertDoesNotThrow(()-> {;
            screeningManager.createScreening(movie, hall, date);
        });

        Assertions.assertEquals(1, screeningManager.getScreeningCount());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            screeningManager.createScreening(movie, hall, date);
        });

        Assertions.assertEquals(1, screeningManager.getScreeningCount());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            screeningManager.createScreening(movie, hall,
                    new GregorianCalendar(
                            2025, Calendar.MARCH, 1,
                            16, 0
                    ).getTime()
            );
        });

        Assertions.assertEquals(1, screeningManager.getScreeningCount());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            screeningManager.createScreening(movie, hall,
                    new GregorianCalendar(
                            2025, Calendar.MARCH, 1,
                            17, 30
                    ).getTime()
            );
        });

        Assertions.assertEquals(1, screeningManager.getScreeningCount());

        Assertions.assertDoesNotThrow(() -> {
            screeningManager.createScreening(movie, hall,
                    new GregorianCalendar(
                            2025, Calendar.MARCH, 1,
                            17, 31
                    ).getTime()
            );
        });

        Assertions.assertEquals(2, screeningManager.getScreeningCount());


        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            screeningManager.createScreening(movie, hall,
                    new GregorianCalendar(
                            2025, Calendar.MARCH, 1,
                            13, 30
                    ).getTime()
            );
        });

        Assertions.assertEquals(2, screeningManager.getScreeningCount());

        Assertions.assertDoesNotThrow(() -> {
            screeningManager.createScreening(movie, hall,
                    new GregorianCalendar(
                            2025, Calendar.MARCH, 1,
                            13, 29
                    ).getTime()
            );
        });

        Assertions.assertEquals(3, screeningManager.getScreeningCount());

        Movie longMovie = movieManager.createMovie(
                "Resan (The Journey)",
                Duration.ofMinutes(873),
                "Documentary",
                15.0,
                directorManager.registerDirector("Peter", "Watkins")
        );

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            screeningManager.createScreening(longMovie, hall,
                    new GregorianCalendar(
                            2025, Calendar.MARCH, 1,
                            10, 0
                    ).getTime()
            );
        });

        Assertions.assertEquals(3, screeningManager.getScreeningCount());

        Assertions.assertDoesNotThrow(() -> {
            screeningManager.createScreening(movie, hall2,
                    new GregorianCalendar(
                            2025, Calendar.MARCH, 1,
                            15, 30
                    ).getTime()
            );
        });

        Assertions.assertEquals(4, screeningManager.getScreeningCount());

    }

    @Test
    void createHallTest(){
        Hall hall = hallManager.createHall("Main Hall", 20, 15);
        Assertions.assertEquals("Main Hall", hall.getName());
        Assertions.assertEquals(20, hall.getColumns());
        Assertions.assertEquals(15, hall.getRows());
        List<Hall> hallList = hallManager.getAll();
        Assertions.assertEquals(1, hallList.size());
        Assertions.assertEquals("Main Hall", hallList.getFirst().getName());
        Assertions.assertEquals(20, hallList.getFirst().getColumns());
        Assertions.assertEquals(15, hallList.getFirst().getRows());
    }

    @Test
    void createTicketTest() {
        Director director = directorManager.registerDirector("Christopher", "Nolan");
        Movie movie = movieManager.createMovie("Inception", Duration.ofMinutes(148), "Sci-Fi", 12.0, director);
        Hall hall = hallManager.createHall("IMAX", 15, 10);
        Date screeningDate = new GregorianCalendar(2024, Calendar.DECEMBER, 20, 20, 0).getTime();
        Screening screening = screeningManager.createScreening(movie, hall, screeningDate);
        Client client = clientManager.registerClient(
                "Alice",
                "Johnson",
                "alice.johnson@example.com",
                new GregorianCalendar(1990, Calendar.JANUARY, 5).getTime(),
                new Address("New York", "10001", "5th Avenue", "1A")
        );
        Ticket ticket = ticketManager.createTicket(screening, client, 5, 7);
        List<Ticket> ticketList = ticketManager.getAll();
        Assertions.assertEquals(1, ticketList.size());
        Assertions.assertEquals(screening, ticketList.getFirst().getScreening());
        Assertions.assertEquals(client, ticketList.getFirst().getClient());
        Assertions.assertEquals(7, ticketList.getFirst().getSeatColumn());
        Assertions.assertEquals(5, ticketList.getFirst().getSeatRow());

        Assertions.assertDoesNotThrow(()-> {;
            ticketManager.createTicket(screening, client, 0, 0);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(screening, client, -1, 0);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(screening, client, 0, -1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(screening, client, hall.getColumns(), 0);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(screening, client, 0, hall.getColumns());
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(screening, client, 0, 0);
        });
    }
}
