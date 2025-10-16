package org.example.managers;

import org.example.Repository;
import org.example.model.Movie;
import org.example.model.persons.Director;

import java.time.Duration;
import java.util.Date;

public class MovieManager {
    private final Repository<Movie> movieRepository;

    public MovieManager(Repository<Movie> movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie createMovie(String title, Date startShowDate, Duration timeDuration, String category, double basicPrice, Director director) {
        Movie newMovie = new Movie(title, startShowDate, timeDuration, category, basicPrice, director);
        return movieRepository.add(newMovie);
    }

    public Movie getMovieByName(String name) {
        throw new UnsupportedOperationException();
    }
}
