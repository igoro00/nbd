package org.example.managers;

import com.mongodb.MongoWriteException;
import org.example.model.ScreeningByMovie;
import org.example.model.TicketByScreening;
import org.example.model.Client;
import org.example.repositories.TicketRepository;

import java.util.List;

public class TicketManager implements AutoCloseable {
    private final TicketRepository repository;

    public TicketManager(TicketRepository repository) {
        this.repository = repository;
    }

    public TicketByScreening createTicket(ScreeningByMovie screeningByMovie, Client client, int seatRow, int seatColumn) {
        try {
            return repository.add(new TicketByScreening(screeningByMovie, client, seatColumn, seatRow));
        } catch (MongoWriteException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<TicketByScreening> getAll(){
        return repository.findAll();
    }

    public long getTicketCount() {
        return repository.countAll();
    }

    @Override
    public void close() throws Exception {
        repository.close();
    }
}
