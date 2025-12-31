package org.example.repositories;

import lombok.Getter;
import org.example.dao.ScreeningByMovieDao;

public class ScreeningRepository extends AbstractCassandraRepository {
    @Getter
    private final ScreeningByMovieDao dao;

    public ScreeningRepository() {
        super();
        this.dao = getMapper().screeningDao();
    }
}
