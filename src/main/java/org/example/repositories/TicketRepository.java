package org.example.repositories;

import lombok.Getter;
import org.example.dao.TicketByScreeningDao;
import org.example.model.Client;
import org.example.model.TicketByScreening;

import java.util.List;

public class TicketRepository extends AbstractCassandraRepository<TicketByScreening> {
    @Getter
    private final TicketByScreeningDao dao;

    public TicketRepository() {
        super();
        this.dao = getMapper().ticketDao();
    }

    @Override
    public void delete(TicketByScreening obj) {
        getDao().delete(obj);
    }

    @Override
    public void update(TicketByScreening obj) {
        getDao().update(obj);
    }

    @Override
    public List<TicketByScreening> getAll() {
        return getDao().getAll().all();
    }

    @Override
    public void add(TicketByScreening obj) throws IllegalArgumentException{
        boolean result = getDao().buyTicket(obj);
        if (!result) {
            throw new IllegalArgumentException();
        }
    }


}
