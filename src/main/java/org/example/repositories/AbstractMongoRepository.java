package org.example.repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.example.model.AbstractEntity;

import java.util.List;

public abstract class AbstractMongoRepository<T extends AbstractEntity> implements AutoCloseable {
    private final ConnectionString connectionString = new ConnectionString(
            "mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=replica_set_single");
    private final MongoCredential credential = MongoCredential.createCredential(
            "admin", "admin", "adminpassword".toCharArray());
    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
                    .automatic(true)
                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                    .build());
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(PojoCodecProvider.builder().build()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();
        mongoClient = MongoClients.create(settings);
        mongoDatabase = mongoClient.getDatabase("absolute_cinema");
    }

    public AbstractMongoRepository() {
        initDbConnection();
    }

    public ClientSession getClientSession() {
        return mongoClient.startSession();
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public abstract T add(T entity);

    public abstract List<T> findAll();

    public abstract long countAll();

    @Override
    public void close() {
//        mongoDatabase.drop();
        mongoClient.close();
    }
}
