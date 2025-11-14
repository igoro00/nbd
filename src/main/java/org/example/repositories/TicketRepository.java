package org.example.repositories;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.example.model.Client;
import org.example.model.Hall;
import org.example.model.Screening;
import org.example.model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketRepository extends AbstractMongoRepository<Ticket>{
    private final MongoCollection<Ticket> collection;

    public TicketRepository() {
        super();
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
        ClientSession session = getClientSession();

        try {
            session.startTransaction();
            MongoCollection<Screening> screeningCollection = getMongoDatabase().getCollection("screenings", Screening.class);
            Screening screening = screeningCollection.find(session, Filters.eq("_id", entity.getScreening().getEntityId())).first();
            if (screening == null) {
                throw new IllegalArgumentException("Invalid screening id");
            }

            MongoCollection<Client> clientCollection = getMongoDatabase().getCollection("clients", Client.class);
            Client client = clientCollection.find(session, Filters.eq("_id", entity.getClient().getEntityId())).first();
            if (client == null) {
                throw new IllegalArgumentException("Invalid client id");
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
