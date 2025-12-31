package org.example.managers;

import org.example.model.Movie;
import org.example.model.ScreeningByMovie;
import org.example.repositories.ScreeningRepository;

import java.util.Date;
import java.util.List;

public class ScreeningManager implements AutoCloseable {
    private final ScreeningRepository repository;

    public ScreeningManager(ScreeningRepository repository) {
        this.repository = repository;
    }

    public long getScreeningCount() {
        return repository.countAll();
    }

    public List<ScreeningByMovie> getAll() {
        return repository.findAll();
    }

    public ScreeningByMovie createScreening(Movie movie, Hall hall, Date screeningDate) {
        return repository.add(new ScreeningByMovie(movie, hall, screeningDate));
    }

    @Override
    public void close() throws Exception {
        repository.close();
    }
}
