package org.example.managers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.RollbackException;
import org.example.repositories.Repository;
import org.example.model.Screening;
import org.example.model.Ticket;
import org.example.model.Client;

import java.util.List;

public class TicketManager {
    private final Repository<Ticket> ticketRepository;

    public TicketManager(EntityManager em) {
        this.ticketRepository = new Repository<>(Ticket.class, em);
    }

    public Ticket createTicket(Screening screening, Client client, int seatRow, int seatColumn) {
        try{
            Ticket newMovie = new Ticket(screening, client, seatColumn, seatRow);
            return ticketRepository.add(newMovie);
        } catch (RollbackException e){
            throw new IllegalArgumentException("Seat is already reserved or out of bounds.");
        }
    }

    public List<Ticket> getAll(){
        return ticketRepository.findAll();
    }

    public int getTicketCount() {
        return ticketRepository.countAll();
    }
}
