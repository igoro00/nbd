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
        try {
            clientManager.registerClient(
                    "John",
                    "Doe",
                    (new SimpleDateFormat("yyyy-MM-dd")).parse("1978.0929"),
                    "example@example.com"
            );
        } catch (ParseException e) {

        }
    }
}
