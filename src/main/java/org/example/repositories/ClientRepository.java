package org.example.repositories;

import com.mongodb.client.MongoCollection;
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
        ArrayList<Client> clients = new ArrayList<>();
        collection.find().into(clients);
        return clients;
    }

    @Override
    public long countAll() {
        return collection.countDocuments();
    }
}
