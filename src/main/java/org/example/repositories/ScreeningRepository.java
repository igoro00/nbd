package org.example.repositories;

import lombok.Getter;
import org.example.dao.ScreeningByMovieDao;
import org.example.model.Client;
import org.example.model.ScreeningByMovie;

import java.util.List;

public class ScreeningRepository extends AbstractCassandraRepository<ScreeningByMovie> {
    @Getter
    private final ScreeningByMovieDao dao;

    public ScreeningRepository() {
        super();
        this.dao = getMapper().screeningDao();
    }

    @Override
    public void delete(ScreeningByMovie obj) {
        getDao().delete(obj);
    }

    @Override
    public void update(ScreeningByMovie obj) {
        getDao().update(obj);
    }

    @Override
    public List<ScreeningByMovie> getAll() {
        return getDao().getAll().all();
    }

    @Override
    public void add(ScreeningByMovie obj) {
        getDao().add(obj);
    }
}
