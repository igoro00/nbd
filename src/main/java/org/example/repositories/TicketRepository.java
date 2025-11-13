package org.example.repositories;

import com.mongodb.client.MongoCollection;
import org.example.model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketRepository extends AbstractMongoRepository<Ticket>{
    private final MongoCollection<Ticket> collection;

    public TicketRepository() {
        super();
        collection = getMongoDatabase().getCollection("tickets", Ticket.class);
    }

    @Override
    public Ticket add(Ticket entity) {
        collection.insertOne(entity);
        return entity;
    }

    @Override
    public List<Ticket> findAll() {
        ArrayList<Ticket> tickets = new ArrayList<>();
        collection.find().into(tickets);
        return tickets;
    }

    @Override
    public long countAll() {
        return collection.countDocuments();
    }
}
