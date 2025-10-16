package org.example.managers;

import jakarta.persistence.EntityManager;
import org.example.Repository;
import org.example.model.Movie;
import org.example.model.persons.Client;
import org.example.model.persons.Director;

import java.time.Duration;
import java.util.Date;
import java.util.List;

public class MovieManager {
    private final Repository<Movie> movieRepository;

    public MovieManager(EntityManager em) {
        this.movieRepository = new Repository<Movie>(Movie.class, em);
    }

    public Movie createMovie(String title, Date startShowDate, Duration timeDuration, String category, double basicPrice, Director director) {
        Movie newMovie = new Movie(title, startShowDate, timeDuration, category, basicPrice, director);
        return movieRepository.add(newMovie);
    }

    public Movie getMovieByName(String name) {
        throw new UnsupportedOperationException();
    }

    public List<Movie> getAll(){
        return movieRepository.findAll();
    }
}
