import org.example.kafka.TicketProducer;
import org.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class KafkaTest {
    private TicketProducer producer;
    private final Ticket exampleTicket = new Ticket(
            new Client(
                    "John",
                    "Doe",
                    new GregorianCalendar(1978, Calendar.SEPTEMBER, 29).getTime(),
                    "example@example.com",
                    new Address(
                            "Łódź",
                            "90-105",
                            "Piotrkowska",
                            "69/8"
                    )
            ),
            new Screening(
                    new Movie(
                            "Inception",
                            Duration.ofMinutes(120),
                            "Sci-Fi",
                            10.0,
                            new Director("Steven", "Spielberg")
                    ),
                    new Hall("sala1", 20, 10),
                    new GregorianCalendar(2025, Calendar.MARCH, 1, 15, 30).getTime()
            ),
            5, 7
    );

    @BeforeEach
    public void setUp() throws Exception {
        producer = new TicketProducer();`
    }

    @Test
    void produceTicket() {
        producer.send(exampleTicket);
    }
}
