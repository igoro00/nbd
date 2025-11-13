package org.example.managers;

import org.example.repositories.MovieRepository;
import org.example.model.Movie;
import org.example.model.Director;

import java.time.Duration;
import java.util.List;

public class MovieManager implements AutoCloseable {
    private final MovieRepository repository;

    public MovieManager() {
        this.repository = new MovieRepository();
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

    public void deleteAllMovies() {
        repository.deleteAll();
    }

    @Override
    public void close() throws Exception {
        repository.close();
    }
}
