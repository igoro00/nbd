package org.example.repositories;

import com.mongodb.client.MongoCollection;
import org.example.model.Hall;

import java.util.ArrayList;
import java.util.List;

public class HallRepository extends AbstractMongoRepository<Hall>{
    private final MongoCollection<Hall> collection;

    public HallRepository() {
        super();
        collection = getMongoDatabase().getCollection("halls", Hall.class);
    }

    @Override
    public Hall add(Hall entity) {
        collection.insertOne(entity);
        return entity;
    }

    @Override
    public List<Hall> findAll() {
        ArrayList<Hall> halls = new ArrayList<>();
        collection.find().into(halls);
        return halls;
    }

    @Override
    public long countAll() {
        return collection.countDocuments();
    }
}
