package org.example.repositories;

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

        CreateCollectionOptions options = new CreateCollectionOptions()
            .validationOptions(
                AbstractMongoRepository.foreignKeyValidator(
                    "fk_screening_movie",
                    "movies",
                    "_id",
                    "movie._id"
                )
            ).validationOptions(
                AbstractMongoRepository.foreignKeyValidator(
                    "fk_screening_hall",
                    "halls",
                    "_id",
                    "hall._id"
                )
            );
        getMongoDatabase().createCollection("screenings", options);
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
        Bson filter = Filters.and(
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
        ArrayList<Screening> arr = new ArrayList<>();
        collection.find(filter).into(arr);
        return arr;
    }

    @Override
    public void deleteAll() {
        collection.deleteMany(new Document());
    }
}
