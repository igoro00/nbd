package org.example.repositories;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.example.model.Movie;

import java.util.ArrayList;
import java.util.List;


public class MovieRepository extends AbstractMongoRepository<Movie>{
    private final MongoCollection<Movie> collection;

    public MovieRepository() {
        super();
        collection = getMongoDatabase().getCollection("movies", Movie.class);
    }

    @Override
    public Movie add(Movie entity) {
        collection.insertOne(entity);
        return entity;
    }

    @Override
    public List<Movie> findAll() {
        ArrayList<Movie> arr = new ArrayList<>();
        collection.find().into(arr);
        return arr;
    }

    @Override
    public long countAll() {
        return collection.countDocuments();
    }

    @Override
    public void deleteAll() {
        collection.deleteMany(new Document());
    }
}

