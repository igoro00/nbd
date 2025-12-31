package org.example.repositories;

import lombok.Getter;
import org.example.dao.TicketByScreeningDao;

public class TicketRepository extends AbstractCassandraRepository {
    @Getter
    private final TicketByScreeningDao dao;

    public TicketRepository() {
        super();
        this.dao = getMapper().ticketDao();
    }
}
