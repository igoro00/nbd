package org.example.repositories;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.model.Hall;
import org.example.model.Movie;
import org.example.model.Screening;
import org.example.model.Ticket;

import java.util.ArrayList;
import java.util.Arrays;
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
        ClientSession session = getClientSession();

        try {
            session.startTransaction();
            MongoCollection<Hall> hallCollection = getMongoDatabase().getCollection("halls", Hall.class);
            Hall hall = hallCollection.find(session, Filters.eq("_id", entity.getHall().getEntityId())).first();
            if (hall == null) {
                throw new IllegalArgumentException("Invalid hall id");
            }

            MongoCollection<Movie> movieCollection = getMongoDatabase().getCollection("movies", Movie.class);
            Movie movie = movieCollection.find(session, Filters.eq("_id", entity.getMovie().getEntityId())).first();
            if (movie == null) {
                throw new IllegalArgumentException("Invalid movie id");
            }
            Screening collidingScreening = collection.find(getCollingScreeningsFilter(
                    entity.getHall(),
                    entity.getStartDate(),
                    new Date(
                            entity.getStartDate().getTime() +
                                    entity.getMovie().getDuration().toMillis()
                    )
            )).first();

            if (collidingScreening != null) {
                throw new IllegalArgumentException("There is already a screening in this hall at the given time.");
            }
            collection.insertOne(session, entity);
            session.commitTransaction();
            return entity;
        } catch (Exception e) {
            session.abortTransaction();
            throw e;
        } finally {
            session.close();
        }
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

    private Bson getCollingScreeningsFilter(Hall hall, Date startTime, Date endTime) {
        return Filters.and(
            Filters.nor(
                Filters.or(
                    Filters.expr(
                        new Document("$lt", Arrays.asList(
                            new Document("$add", Arrays.asList("$start_date", "$movie.duration")),
                            startTime
                        ))
                    ),
                    Filters.gt("start_date", endTime)
                )
            ),
            Filters.eq("hall._id", hall.getEntityId())
        );
    }

    @Override
    public void deleteAll() {
        collection.deleteMany(new Document());
    }
}
