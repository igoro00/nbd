package org.example.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.example.model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketRepository extends AbstractMongoRepository<Ticket>{
    private final MongoCollection<Ticket> collection;

    public TicketRepository() {
        super();
        CreateCollectionOptions options = new CreateCollectionOptions()
            .validationOptions(
                AbstractMongoRepository.foreignKeyValidator(
                    "ticket_client_fk",
                    "clients",
                    "_id",
                    "client._id"
                )
        );
        getMongoDatabase().createCollection("tickets", options);
        collection = getMongoDatabase().getCollection("tickets", Ticket.class);

        IndexOptions indexOptions = new IndexOptions().unique(true);
        collection.createIndex(Indexes.ascending(
                "seat_row",
                "seat_column",
                "screening._id"
        ), indexOptions);
    }

    @Override
    public Ticket add(Ticket entity) {
        collection.insertOne(entity);
        return entity;
    }

    @Override
    public List<Ticket> findAll() {
        ArrayList<Ticket> arr = new ArrayList<>();
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
