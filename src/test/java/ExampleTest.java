import org.example.managers.ClientManager;
import org.example.managers.MovieManager;
import org.example.managers.ScreeningManager;
import org.example.managers.TicketManager;
import org.example.model.Client;
import org.example.model.Movie;
import org.example.model.ScreeningByMovie;
import org.example.model.TicketByScreening;
import org.example.repositories.ClientRepository;
import org.example.repositories.MovieRepository;
import org.example.repositories.ScreeningRepository;
import org.example.repositories.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import java.util.UUID;

class ExampleTest {
    private ClientManager clientManager;
    private MovieManager movieManager;
    private TicketManager ticketManager;
    private ScreeningManager screeningManager;

    private ClientRepository clientRepository;
    private MovieRepository movieRepository;
    private ScreeningRepository screeningRepository;
    private TicketRepository ticketRepository;

    private static Instant atDate(int year, int monthZeroBased, int day) {
        return LocalDate.of(year, monthZeroBased + 1, day).atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    @BeforeEach
    public void setUp() throws Exception {
        try (ClientRepository repo = new ClientRepository()) {
            CqlSession session = repo.getSession();
            session.execute("TRUNCATE clients");
            session.execute("TRUNCATE movies");
            session.execute("TRUNCATE screenings_by_movie");
            session.execute("TRUNCATE tickets_by_screening");
        }
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
    public void clientCrudWorks() {
        Instant dob = atDate(1990, Calendar.JANUARY, 1);
        Client client = clientManager.registerClient("John", "Doe", "john@example.com", dob);
        Assertions.assertNotNull(client);

        Client fetched = clientManager.getById(client.getClientId());
        Assertions.assertNotNull(fetched);
        Assertions.assertEquals(client.getEmail(), fetched.getEmail());

        // update
        fetched.setEmail("john.new@example.com");
        clientManager.update(fetched);
        Client updated = clientManager.getById(client.getClientId());
        Assertions.assertEquals("john.new@example.com", updated.getEmail());

        // delete
        clientManager.delete(updated);
        Client deleted = clientManager.getById(client.getClientId());
        Assertions.assertNull(deleted);
    }

    @Test
    public void movieCrudWorks() {
        Movie movie = movieManager.createMovie("Test Movie", Duration.ofMinutes(120), "Drama", 10.0, "Director");
        Assertions.assertNotNull(movie);

        Movie fetched = movieManager.getById(movie.getMovieId());
        Assertions.assertNotNull(fetched);
        Assertions.assertEquals(movie.getTitle(), fetched.getTitle());

        // update
        fetched.setPrice(12.5);
        movieManager.update(fetched);
        Movie updated = movieManager.getById(movie.getMovieId());
        Assertions.assertEquals(12.5, updated.getPrice());

        // delete
        movieManager.delete(updated);
        Movie deleted = movieManager.getById(movie.getMovieId());
        Assertions.assertNull(deleted);
    }

    @Test
    public void screeningCrudWorks() {
        // need a movie for screening
        Movie movie = movieManager.createMovie("Screen Test", Duration.ofMinutes(90), "Action", 8.0, "Dir");

        Instant start = Instant.now();
        ScreeningByMovie screening = screeningManager.createScreening(movie, "Hall A", start);
        Assertions.assertNotNull(screening);

        List<ScreeningByMovie> byMovie = screeningManager.getByMovie(movie.getMovieId());
        Assertions.assertTrue(byMovie.stream().anyMatch(s -> s.getScreeningId().equals(screening.getScreeningId())));

        // update
        screening.setHallName("Hall B");
        screeningManager.update(screening);
        List<ScreeningByMovie> afterUpdate = screeningManager.getByMovie(movie.getMovieId());
        Assertions.assertTrue(afterUpdate.stream().anyMatch(s -> "Hall B".equals(s.getHallName())));

        // delete
        screeningManager.delete(screening);
        List<ScreeningByMovie> afterDelete = screeningManager.getByMovie(movie.getMovieId());
        Assertions.assertFalse(afterDelete.stream().anyMatch(s -> s.getScreeningId().equals(screening.getScreeningId())));
    }

    @Test
    public void ticketCrudWorks() {
        // create client, movie, screening
        Instant dob = atDate(1995, Calendar.FEBRUARY, 2);
        Client client = clientManager.registerClient("Alice", "Smith", "alice@example.com", dob);
        Movie movie = movieManager.createMovie("Ticket Movie", Duration.ofMinutes(100), "Comedy", 9.0, "Dir");
        ScreeningByMovie screening = screeningManager.createScreening(movie, "Main Hall", Instant.now());

        // create ticket
        TicketByScreening ticket = ticketManager.createTicket(screening, client, 1, 1);
        Assertions.assertNotNull(ticket);

        List<TicketByScreening> tickets = ticketManager.getByScreening(screening.getScreeningId());
        Assertions.assertTrue(tickets.stream().anyMatch(t -> t.getClientId().equals(client.getClientId())));

        // update
        ticketManager.update(ticket);

        // delete
        ticketManager.delete(ticket);
        List<TicketByScreening> afterDelete = ticketManager.getByScreening(screening.getScreeningId());
        Assertions.assertFalse(afterDelete.stream().anyMatch(t -> t.getClientId().equals(client.getClientId())));
    }

    @Test
    public void ticketDoubleBookingFails() {
        Instant dob = atDate(1992, Calendar.MARCH, 3);
        Client clientA = clientManager.registerClient("Bob", "A", "bob.a@example.com", dob);
        Client clientB = clientManager.registerClient("Bob", "B", "bob.b@example.com", dob);
        Movie movie = movieManager.createMovie("DoubleBook", Duration.ofMinutes(95), "Thriller", 7.0, "Dir");
        ScreeningByMovie screening = screeningManager.createScreening(movie, "Hall X", Instant.now());

        // first purchase should succeed
        TicketByScreening ticket1 = ticketManager.createTicket(screening, clientA, 2, 2);
        Assertions.assertNotNull(ticket1);

        // second purchase for same seat should throw
        Assertions.assertThrows(IllegalArgumentException.class, () -> ticketManager.createTicket(screening, clientB, 2, 2));
    }

}
