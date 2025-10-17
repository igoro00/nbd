package org.example.managers;

import jakarta.persistence.EntityManager;
import org.example.model.Hall;
import org.example.model.Movie;
import org.example.model.Screening;
import org.example.repositories.ScreeningRepository;

import java.util.Date;
import java.util.List;

public class ScreeningManager {
    private final ScreeningRepository screeningRepository;
    private final EntityManager em;

    public ScreeningManager(EntityManager em) {
        this.em = em;

        this.screeningRepository = new ScreeningRepository(this.em);
    }

    public int getScreeningCount() {
        return screeningRepository.countAll();
    }

    public List<Screening> getAll() {
        return screeningRepository.findAll();
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
