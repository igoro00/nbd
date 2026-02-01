package org.example.kafka;

import org.example.managers.TicketManager;
import org.example.repositories.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaApp {

    private static final Logger log = LoggerFactory.getLogger(KafkaApp.class);

    public static void main(String[] args) throws InterruptedException {
        TicketRepository ticketRepository = new TicketRepository();
        TicketManager ticketManager = new TicketManager(ticketRepository);
        TicketConsumer consumer1 = new TicketConsumer("consumer-1", ticketManager);
        TicketConsumer consumer2 = new TicketConsumer("consumer-2", ticketManager);
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            log.error(String.valueOf(throwable));
            try {
                consumer1.close();
                consumer2.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.exit(1);
        });
        consumer1.start();
        consumer2.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                consumer1.close();
                consumer2.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));
        consumer1.getThread().join();
        consumer2.getThread().join();
    }
}
