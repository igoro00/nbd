package org.example.managers;

import org.example.model.Screening;
import org.example.model.Ticket;
import org.example.model.Client;
import org.example.repositories.TicketRepository;

import java.util.List;

public class TicketManager implements AutoCloseable {
    private final TicketRepository repository;

    public TicketManager() {
        this.repository = new TicketRepository();
    }

    public Ticket createTicket(Screening screening, Client client, int seatRow, int seatColumn) {
        Ticket newMovie = new Ticket(screening, client, seatColumn, seatRow);
        return repository.add(newMovie);
    }

    public List<Ticket> getAll(){
        return repository.findAll();
    }

    public long getTicketCount() {
        return repository.countAll();
    }

    public void deleteAllTickets() {
        repository.deleteAll();
    }

    @Override
    public void close() throws Exception {
        repository.close();
    }
}
