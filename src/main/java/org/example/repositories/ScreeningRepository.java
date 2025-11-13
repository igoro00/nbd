package org.example.repositories;

import com.mongodb.client.MongoCollection;
import org.example.model.Hall;
import org.example.model.Movie;
import org.example.model.Screening;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScreeningRepository extends AbstractMongoRepository<Screening> {
    private final MongoCollection<Screening> collection;
    public ScreeningRepository() {
        super();
        collection = getMongoDatabase().getCollection("screenings", Screening.class);
    }

    @Override
    public Screening add(Screening entity) {
        collection.insertOne(entity);
        return entity;
    }

    @Override
    public List<Screening> findAll() {
        ArrayList<Screening> screenings = new ArrayList<>();
        collection.find().into(screenings);
        return screenings;
    }

    @Override
    public long countAll() {
        return collection.countDocuments();
    }

    public List<Screening> findByHallAndTime(Hall hall, Date startTime, Date endTime) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        CriteriaQuery<Screening> query = cb.createQuery(Screening.class);
        Root<Screening> screening = query.from(Screening.class);
        Join<Screening, Movie> movie = screening.join("movie");
        query.select(screening).where(
            cb.not(cb.or(
                cb.lessThan(
                        cb.function(
                                "date_add",
                                Date.class,
                                screening.get("startDate"),
                                movie.get("timeDuration")
                        ),
                        startTime
                ),
                cb.greaterThan(screening.get("startDate"), endTime))
            ),
            cb.equal(screening.get("hall"), hall)
        );
        return this.getEM().createQuery(query).getResultList();
    }
}
