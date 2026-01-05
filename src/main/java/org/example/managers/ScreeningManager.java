package org.example.managers;

import org.example.UUID7;
import org.example.repositories.ScreeningRepository;
import org.example.model.Movie;
import org.example.model.ScreeningByMovie;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ScreeningManager {
    private final ScreeningRepository repository;

    public ScreeningManager(ScreeningRepository repository) {
        this.repository = repository;
    }

    public List<ScreeningByMovie> getAll() {
        return repository.getAll();
    }

    public ScreeningByMovie createScreening(Movie movie, String hallName, Instant screeningDate) {
        ScreeningByMovie screening = new ScreeningByMovie(
                movie.getMovieId(),
                UUID7.randomUUID(),
                movie.getTitle(),
                hallName,
                screeningDate,
                movie.getDuration()
        );
        repository.add(screening);
        return screening;
    }

    public void delete(ScreeningByMovie screening) {
        repository.delete(screening);
    }

    public void update(ScreeningByMovie screening) {
        repository.update(screening);
    }

    public List<ScreeningByMovie> getByMovie(UUID movieId) {
        return repository.getByMovieId(movieId);
    }
}
