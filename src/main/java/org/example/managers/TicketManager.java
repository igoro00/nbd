package org.example.managers;

import jakarta.persistence.EntityManager;
import org.example.repositories.Repository;
import org.example.model.Screening;
import org.example.model.Ticket;
import org.example.model.persons.Client;

import java.util.List;

public class TicketManager {
    private final Repository<Ticket> ticketRepository;

    public TicketManager(EntityManager em) {
        this.ticketRepository = new Repository<Ticket>(Ticket.class, em);
    }

    public Ticket createTicket(Screening screening, Client client, int seatRow, int seatColumn) {
        Ticket newMovie = new Ticket(screening, client, seatColumn, seatRow);
        return ticketRepository.add(newMovie);
    }

    public Ticket getMovieByName(String name) {
        throw new UnsupportedOperationException();
    }

    public List<Ticket> getAll(){
        return ticketRepository.findAll();
    }
}
