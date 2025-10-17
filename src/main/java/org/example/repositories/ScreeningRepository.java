package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.example.model.Hall;
import org.example.model.Movie;
import org.example.model.Screening;

import java.util.Date;
import java.util.List;

public class ScreeningRepository extends Repository<Screening> {
    public ScreeningRepository(EntityManager em) {
        super(Screening.class, em);
    }

    public List<Screening> findByHallAndTime(Hall hall, Date startTime, Date endTime) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        CriteriaQuery<Screening> query = cb.createQuery(Screening.class);
        Root<Screening> screening = query.from(Screening.class);
        Join<Screening, Movie> movie = screening.join("movie");
        Expression<Date> endDate = cb.function(
        "CAST",
        Date.class,
        cb.function("(?1 + (?2 * interval '1 second'))", Date.class,
        screening.get("startDate"),
        movie.get("timeDuration"))
        );
        query.select(screening).where(
                cb.not(cb.or(
                        cb.lessThan(endDate, startTime),
                        cb.greaterThan(screening.get("startDate"), endTime))
                )
        );
        return this.getEM().createQuery(query).getResultList();
    }
}
