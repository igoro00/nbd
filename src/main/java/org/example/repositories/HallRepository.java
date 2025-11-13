package org.example.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.example.model.Hall;

import java.util.ArrayList;
import java.util.List;

public class HallRepository extends AbstractMongoRepository<Hall>{
    private final MongoCollection<Hall> collection;

    public HallRepository() {
        super();
        collection = getMongoDatabase().getCollection("halls", Hall.class);
        IndexOptions options = new IndexOptions().unique(true);
        collection.createIndex(Indexes.ascending("name"), options);
    }

    @Override
    public Hall add(Hall entity) {
        collection.insertOne(entity);
        return entity;
    }

    @Override
    public List<Hall> findAll() {
        ArrayList<Hall> arr = new ArrayList<>();
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
