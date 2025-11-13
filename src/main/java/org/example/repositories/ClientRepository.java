package org.example.repositories;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.example.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientRepository extends AbstractMongoRepository<Client> {
    private final MongoCollection<Client> collection;

    public ClientRepository() {
        super();
        collection = getMongoDatabase().getCollection("clients", Client.class);
    }

    @Override
    public Client add(Client entity) {
        collection.insertOne(entity);
        return entity;
    }

    @Override
    public List<Client> findAll() {
        ArrayList<Client> arr = new ArrayList<>();
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
