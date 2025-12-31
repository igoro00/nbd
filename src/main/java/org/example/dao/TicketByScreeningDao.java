package org.example.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.model.TicketByScreening;

import java.util.UUID;

@Dao
public interface TicketByScreeningDao {
    @Select
    PagingIterable<TicketByScreening> getTicketsForScreening(UUID screeningId);

    // used for buying tickets (doesn't permit double booking the same seat)
    // returns a bool telling you if it worked or not
    @Insert(ifNotExists = true)
    boolean buyTicket(TicketByScreening ticket);

    @Update
    void update(TicketByScreening ticket);

    @Delete
    void delete(TicketByScreening ticket);
}
