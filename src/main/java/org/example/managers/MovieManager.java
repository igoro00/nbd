package org.example.managers;

import org.example.Repository;
import org.example.model.Movie;

public class MovieManager {
    private final Repository<Movie> repository;

    public MovieManager(Repository<Movie> repository) {
        this.repository = repository;
    }

    public Movie getMovieByName(String name) {
        throw new UnsupportedOperationException();
    }
}
