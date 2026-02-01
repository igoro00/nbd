import org.example.kafka.TicketConsumer;
import org.example.kafka.TicketProducer;
import org.example.managers.TicketManager;
import org.example.model.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;

@ExtendWith(MockitoExtension.class)
public class KafkaTest {
    private TicketProducer producer;
    private TicketConsumer consumer1;
    private TicketConsumer consumer2;

    @Mock
    private TicketManager ticketManager;

    private Ticket createExampleTicket() {
        return
                new Ticket(
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
    }

    @BeforeEach
    void setUp(){
        producer = new TicketProducer();
        consumer1 = new TicketConsumer("consumer-1", ticketManager);
        consumer2 = new TicketConsumer("consumer-2", ticketManager);
    }
    @AfterEach
    void tearDown() throws Exception {
        producer.close();
        consumer1.close();
        consumer2.close();
    }

    @Test
    void produceTicket() {
        assertDoesNotThrow(()->producer.send(createExampleTicket()));
    }

    @Test
    void produceConsume(){
        Ticket exampleTicket = createExampleTicket();
        consumer1.start();
        consumer2.start();
        producer.send(exampleTicket);

        Mockito.verify(ticketManager, timeout(5000)).createTicket(exampleTicket);
    }

    @Test
    void idempotencyTest() throws InterruptedException {
        Ticket exampleTicket = createExampleTicket();
        consumer1.start();
        consumer2.start();
        producer.send(exampleTicket);
        Mockito.verify(ticketManager, timeout(5000)).createTicket(exampleTicket);
        consumer1.close();
        consumer2.close();
        Mockito.clearInvocations(ticketManager);

        consumer1.start();
        consumer2.start();
        Ticket newTicket = createExampleTicket();
        assertNotEquals(exampleTicket, newTicket);
        producer.send(newTicket);

        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        Mockito.verify(ticketManager, timeout(5000).atLeastOnce()).createTicket(captor.capture());
        assertThat(captor.getAllValues()).doesNotContain(exampleTicket).contains(newTicket);
    }
}
