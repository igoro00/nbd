import org.example.Repository;
import org.example.managers.ClientManager;
import org.example.model.persons.Client;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Testcontainers
class ExampleTest {
    @Container
    private static PostgreSQLContainer postgresqlContainer = (PostgreSQLContainer) new
            PostgreSQLContainer(DockerImageName.parse("postgres:17"))
            .withDatabaseName("nbddb")
            .withUsername("nbd")
            .withPassword("nbdpassword")
            .withExposedPorts(5432);

    private Repository<Client> clientRepository;
    private ClientManager clientManager;

    @BeforeEach
    public void connect(){
        this.clientRepository = new Repository<Client>(Client.class);
        this.clientManager = new ClientManager(clientRepository);
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

}
