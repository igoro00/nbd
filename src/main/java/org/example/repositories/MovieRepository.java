package org.example.repositories;

import lombok.Getter;
import org.example.dao.MovieDao;
import org.example.model.Movie;

import java.util.List;
import java.util.UUID;

public class MovieRepository extends AbstractCassandraRepository<Movie> {
    private final MovieDao dao;

    public MovieRepository() {
        super();
        this.dao = getMapper().movieDao();
    }

    @Override
    public void delete(Movie obj) {
        this.dao.delete(obj);
    }

    @Override
    public void update(Movie obj) {
        this.dao.update(obj);
    }

    @Override
    public List<Movie> getAll() {
        return this.dao.getAll().all();
    }

    public Movie getById(UUID id){
        return this.dao.getByMovieId(id).one();
    }

    @Override
    public void add(Movie obj) {
        this.dao.add(obj);
    }
}
