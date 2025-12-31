package org.example.repositories;

import lombok.Getter;
import org.example.dao.MovieDao;
import org.example.model.Movie;

import java.util.List;

public class MovieRepository extends AbstractCassandraRepository<Movie> {
    @Getter
    private final MovieDao dao;

    public MovieRepository() {
        super();
        this.dao = getMapper().movieDao();
    }

    @Override
    public void delete(Movie obj) {
        getDao().delete(obj);
    }

    @Override
    public void update(Movie obj) {
        getDao().update(obj);
    }

    @Override
    public List<Movie> getAll() {
        return getDao().getAll().all();
    }

    @Override
    public void add(Movie obj) {
        getDao().add(obj);
    }
}
