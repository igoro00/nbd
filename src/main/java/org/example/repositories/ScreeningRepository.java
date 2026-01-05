package org.example.repositories;

import lombok.Getter;
import org.example.dao.ScreeningByMovieDao;
import org.example.model.ScreeningByMovie;

import java.util.List;
import java.util.UUID;

public class ScreeningRepository extends AbstractCassandraRepository<ScreeningByMovie> {
    private final ScreeningByMovieDao dao;

    public ScreeningRepository() {
        super();
        this.dao = getMapper().screeningDao();
    }

    @Override
    public void delete(ScreeningByMovie obj) {
        this.dao.delete(obj);
    }

    @Override
    public void update(ScreeningByMovie obj) {
        this.dao.update(obj);
    }

    @Override
    public List<ScreeningByMovie> getAll() {
        return this.dao.getAll().all();
    }

    public List<ScreeningByMovie> getByMovieId(UUID movieId){
        return this.dao.getByMovieId(movieId).all();
    }

    @Override
    public void add(ScreeningByMovie obj) {
        this.dao.add(obj);
    }
}
