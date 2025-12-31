package org.example.managers;

import org.example.model.Client;
import org.example.model.ScreeningByMovie;
import org.example.model.TicketByScreening;
import org.example.repositories.TicketRepository;

import java.util.List;

public class TicketManager {
    private final TicketRepository repository;

    public TicketManager(TicketRepository repository) {
        this.repository = repository;
    }

    public TicketByScreening createTicket(ScreeningByMovie screening, Client client, int seatRow, int seatColumn) throws IllegalArgumentException {
        TicketByScreening ticket = new TicketByScreening(screening.getScreeningId(), client.getClientId(), seatColumn, seatRow);
        repository.add(ticket);
        return ticket;
    }

    public List<TicketByScreening> getAll() {
        return repository.getAll();
    }

}
