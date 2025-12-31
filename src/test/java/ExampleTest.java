import org.example.managers.*;
import org.example.model.*;
import org.example.model.Client;
import org.example.mappers.*;
import org.example.repositories.ClientRepository;
import org.example.repositories.MovieRepository;
import org.example.repositories.ScreeningRepository;
import org.example.repositories.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

class ExampleTest {
    private ClientManager clientManager;
    private MovieManager movieManager;
    private TicketManager ticketManager;
    private ScreeningManager screeningManager;

    private ClientRepository clientRepository;
    private MovieRepository movieRepository;
    private ScreeningRepository screeningRepository;
    private TicketRepository ticketRepository;
    @BeforeEach
    public void setUp() {
        clientRepository = new ClientRepository();
        movieRepository = new MovieRepository();
        screeningRepository = new ScreeningRepository();
        ticketRepository = new TicketRepository();

        this.clientManager = new ClientManager(clientRepository);
        this.movieManager = new MovieManager(movieRepository);
        this.screeningManager = new ScreeningManager(screeningRepository);
        this.ticketManager = new TicketManager(ticketRepository);
    }

    @AfterEach
    public void close() {
        this.clientRepository.close();
        this.movieRepository.close();
        this.screeningRepository.close();
        this.ticketRepository.close();
    }

    @Test
    void isRepository(){
        Assertions.assertNotNull(clientManager);
    }

    @Test
    void createClientTest() {
        Date date = new GregorianCalendar(1978, Calendar.SEPTEMBER, 29).getTime();

        clientManager.registerClient(
            "Martin",
            "Smith",
            "martin.smith@example.com",
            date
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
    void createMovieTest(){
        Duration duration = Duration.ofMinutes(120);
        Movie movie = movieManager.createMovie(
            "Inception",
            duration,
            "Sci-Fi",
            10.0,
            "Steven",
            "Spielberg"
            );
        Assertions.assertEquals("Inception", movie.getTitle());
        Assertions.assertEquals(duration, movie.getDuration());
        Assertions.assertEquals("Sci-Fi", movie.getCategory());
        Assertions.assertEquals(10.0, movie.getPrice());
        Assertions.assertEquals("Steven", movie.getDirector().getFirstName());
        Assertions.assertEquals("Spielberg", movie.getDirector().getLastName());
        List<Movie> movieList = movieManager.getAll();
        Assertions.assertEquals(1, movieList.size());
        Assertions.assertEquals("Inception", movieList.getFirst().getTitle());
        Assertions.assertEquals(duration, movieList.getFirst().getDuration());
        Assertions.assertEquals("Sci-Fi", movieList.getFirst().getCategory());
        Assertions.assertEquals(10.0, movieList.getFirst().getPrice());
        Assertions.assertEquals("Steven", movieList.getFirst().getDirector().getFirstName());
        Assertions.assertEquals("Spielberg", movieList.getFirst().getDirector().getLastName());
    }

    @Test
    void screeningTest() {
        Movie movie = movieManager.createMovie("Jurassic Park", Duration.ofMinutes(120), "Adventure", 15.0, "Steven", "Spielberg");
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
                "Peter",
                "Watkins"
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
        Movie movie = movieManager.createMovie("Inception", Duration.ofMinutes(148), "Sci-Fi", 12.0, "Christopher", "Nolan");
        Hall hall = hallManager.createHall("IMAX", 15, 10);
        Date screeningDate = new GregorianCalendar(2024, Calendar.DECEMBER, 20, 20, 0).getTime();
        ScreeningByMovie screeningByMovie = screeningManager.createScreening(movie, hall, screeningDate);
        Client client = clientManager.registerClient(
                "Alice",
                "Johnson",
                "alice.johnson@example2.com",
                new GregorianCalendar(1990, Calendar.JANUARY, 5).getTime(),
                new Address("New York", "10001", "5th Avenue", "1A")
        );
        ticketManager.createTicket(screeningByMovie, client, 5, 7);
        List<TicketByScreening> ticketByScreeningList = ticketManager.getAll();
        Assertions.assertEquals(1, ticketByScreeningList.size());
        Assertions.assertEquals(screeningByMovie, ticketByScreeningList.getFirst().getScreening());
        Assertions.assertEquals(client, ticketByScreeningList.getFirst().getClient());
        Assertions.assertEquals(7, ticketByScreeningList.getFirst().getSeatColumn());
        Assertions.assertEquals(5, ticketByScreeningList.getFirst().getSeatRow());

        Assertions.assertDoesNotThrow(()-> {;
            ticketManager.createTicket(screeningByMovie, client, 0, 0);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(screeningByMovie, client, -1, 0);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(screeningByMovie, client, 0, -1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(screeningByMovie, client, hall.getColumns(), 0);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(screeningByMovie, client, 0, hall.getColumns());
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(screeningByMovie, client, 0, 0);
        });
    }
}
