package org.example.managers;

import org.example.model.Hall;
import org.example.model.Movie;
import org.example.model.Screening;
import org.example.repositories.ScreeningRepository;

import java.util.Date;
import java.util.List;

public class ScreeningManager implements AutoCloseable {
    private final ScreeningRepository repository;

    public ScreeningManager() {
        this.repository = new ScreeningRepository();
    }

    public long getScreeningCount() {
        return repository.countAll();
    }

    public List<Screening> getAll() {
        return repository.findAll();
    }

    public Screening createScreening(Movie movie, Hall hall, Date screeningDate) {
        return repository.add(new Screening(movie, hall, screeningDate));
    }

    public void deleteAllScreenings() {
        repository.deleteAll();
    }

    @Override
    public void close() throws Exception {
        repository.close();
    }
}
