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
        List<Screening> collidingScreenings = repository.findByHallAndTime(hall, screeningDate, new Date(screeningDate.getTime() + movie.getDuration().toMillis()));
        if(!collidingScreenings.isEmpty()){
//            em.getTransaction().rollback();
            throw new IllegalArgumentException("There is already a screening in this hall at the given time.");
        }
        Screening newScreening = new Screening(movie, hall, screeningDate);
        newScreening =  repository.add(newScreening);
//        em.getTransaction().commit();
        return newScreening;
    }

    public void deleteAllScreenings() {
        repository.deleteAll();
    }

    @Override
    public void close() throws Exception {
        repository.close();
    }
}
