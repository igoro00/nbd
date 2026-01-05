package org.example.managers;

import lombok.RequiredArgsConstructor;
import org.example.UUID7;
import org.example.model.Movie;
import org.example.repositories.MovieRepository;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class MovieManager {
    private final MovieRepository repository;

    public Movie createMovie(String title, Duration timeDuration, String category, double basicPrice, String directorName) {
        Movie newMovie = new Movie(UUID7.randomUUID(), title, timeDuration, category, basicPrice, directorName);
        repository.add(newMovie);
        return newMovie;
    }

    public List<Movie> getAll() {
        return repository.getAll();
    }

    public Movie getById(UUID id) {
        return repository.getById(id);
    }

    public void delete(Movie movie) {
        repository.delete(movie);
    }

    public void update(Movie movie) {
        repository.update(movie);
    }
}
