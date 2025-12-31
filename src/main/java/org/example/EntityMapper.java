package org.example;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import org.example.dao.ClientDao;
import org.example.dao.MovieDao;
import org.example.dao.ScreeningByMovieDao;
import org.example.dao.TicketByScreeningDao;

@Mapper
public interface EntityMapper {
    @DaoFactory
    ClientDao clientDao();

    @DaoFactory
    MovieDao movieDao();

    @DaoFactory
    ScreeningByMovieDao screeningDao();

    @DaoFactory
    TicketByScreeningDao ticketDao();
}
