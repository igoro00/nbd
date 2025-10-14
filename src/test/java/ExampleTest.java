import org.example.model.persons.Client;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

@Testcontainers
public class ExampleTest {
    @Container
    private static PostgreSQLContainer postgresqlContainer = (PostgreSQLContainer) new
            PostgreSQLContainer(DockerImageName.parse("postgres:17"))
            .withDatabaseName("nbddb")
            .withUsername("nbd")
            .withPassword("nbdpassword")
            .withExposedPorts(5432);

    @Test
    public void costamTest(){
        Client c = new Client("John", "Doe", new Date(), "john.doe@example.com");
        System.out.println("hello from testcos");
    }
}
