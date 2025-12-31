package org.example.managers;

import org.example.mappers.AbstractRepository;
import org.example.model.Movie;

import java.time.Duration;
import java.util.List;

public class MovieManager implements AutoCloseable {
    private final AbstractRepository<Movie> repository;

    public MovieManager(AbstractRepository<Movie> repository) {
        this.repository = repository;
    }

    public Movie createMovie(
            String title,
            Duration timeDuration,
            String category,
            double basicPrice,
            String directorFirstName,
            String directorLastName
    ) {
        Director director = new Director(directorFirstName, directorLastName);
        Movie newMovie = new Movie(title, timeDuration, category, basicPrice, director);
        return repository.add(newMovie);
    }

    public List<Movie> getAll() {
        return repository.findAll();
    }

    @Override
    public void close() throws Exception {
        repository.close();
    }
}
