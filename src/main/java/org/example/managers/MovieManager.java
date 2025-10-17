package org.example.managers;

import jakarta.persistence.EntityManager;
import org.example.repositories.Repository;
import org.example.model.Hall;
import org.example.model.Movie;
import org.example.model.Screening;
import org.example.model.persons.Director;
import org.example.repositories.ScreeningRepository;

import java.time.Duration;
import java.util.Date;
import java.util.List;

public class MovieManager {
    private final Repository<Movie> movieRepository;
    private final ScreeningRepository screeningRepository;
    private final EntityManager em;

    public MovieManager(EntityManager em) {
        this.em = em;
        this.movieRepository = new Repository<Movie>(Movie.class, this.em);
        this.screeningRepository = new ScreeningRepository(this.em);
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

    public Screening createScreening(Movie movie, Hall hall, Date screeningDate) {
        em.getTransaction().begin();
        List<Screening> collidingScreenings = screeningRepository.findByHallAndTime(hall, screeningDate, new Date(screeningDate.getTime() + movie.getTimeDuration().toMillis()));
        if(!collidingScreenings.isEmpty()){
            em.getTransaction().rollback();
            throw new IllegalArgumentException("There is already a screening in this hall at the given time.");
        }
        Screening newScreening = new Screening(movie, hall, screeningDate);
        newScreening =  screeningRepository.addWithoutTransaction(newScreening);
        em.getTransaction().commit();
        return newScreening;
    }
}
