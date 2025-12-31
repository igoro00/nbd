package org.example.repositories;

import lombok.Getter;
import org.example.dao.ClientDao;
import org.example.dao.MovieDao;
import org.example.mappers.ClientMapperBuilder;

public class MovieRepository extends AbstractCassandraRepository {
    @Getter
    private final MovieDao dao;

    public MovieRepository() {
        super();
        this.dao = getMapper().movieDao();
    }
}
