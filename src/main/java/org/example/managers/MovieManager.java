package org.example.managers;

import jakarta.persistence.EntityManager;
import org.example.repositories.Repository;
import org.example.model.Movie;
import org.example.model.Director;

import java.time.Duration;
import java.util.List;

public class MovieManager {
    private final Repository<Movie> movieRepository;

    public MovieManager(EntityManager em) {
        this.movieRepository = new Repository<Movie>(Movie.class, em);
    }

    public Movie createMovie(String title, Duration timeDuration, String category, double basicPrice, Director director) {
        Movie newMovie = new Movie(title, timeDuration, category, basicPrice, director);
        return movieRepository.add(newMovie);
    }

    public Movie getMovieByName(String name) {
        throw new UnsupportedOperationException();
    }

    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    public int getMovieCount() {
        return movieRepository.countAll();
    }
}
