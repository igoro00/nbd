package org.example.repositories;

import lombok.Getter;
import org.example.dao.TicketByScreeningDao;
import org.example.model.TicketByScreening;

import java.util.List;
import java.util.UUID;

public class TicketRepository extends AbstractCassandraRepository<TicketByScreening> {
    private final TicketByScreeningDao dao;

    public TicketRepository() {
        super();
        this.dao = getMapper().ticketDao();
    }

    @Override
    public void delete(TicketByScreening obj) {
        this.dao.delete(obj);
    }

    @Override
    public void update(TicketByScreening obj) {
        this.dao.update(obj);
    }

    @Override
    public List<TicketByScreening> getAll() {
        return this.dao.getAll().all();
    }

    public List<TicketByScreening> getByScreeningId(UUID screeningId) {
        return this.dao.getByScreeningId(screeningId).all();
    }

    @Override
    public void add(TicketByScreening obj) throws IllegalArgumentException{
        boolean result = this.dao.buyTicket(obj);
        if (!result) {
            throw new IllegalArgumentException();
        }
    }
}
