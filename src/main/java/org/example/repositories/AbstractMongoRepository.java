package org.example.repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.example.codecs.CustomCodecProvider;
import org.example.codecs.DurationCodec;
import org.example.model.AbstractEntity;

import java.util.Arrays;
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
                        CodecRegistries.fromProviders(new CustomCodecProvider()),
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

    public abstract void deleteAll();

    public void dropDatabase() {
        mongoDatabase.drop();
    }

    public static ValidationOptions foreignKeyValidator(String constraintName, String foreignCollectionName, String foreignField, String localField) {
        // Build the $lookup stage
        Document lookupStage = new Document("from", foreignCollectionName)
            .append("localField", localField)
            .append("foreignField", foreignField)
            .append("as", constraintName);

        // Build the validation expression
        Document validator = new Document("$expr",
            new Document("$gt", Arrays.asList(
                new Document("$size",
                    new Document("$ifNull", Arrays.asList(
                        new Document("$lookup", lookupStage),
                            List.of()  // empty array as fallback
                    ))
                ),
                0
            ))
        );

        return new com.mongodb.client.model.ValidationOptions()
            .validator(validator)
            .validationLevel(ValidationLevel.STRICT)
            .validationAction(ValidationAction.ERROR);
    }

    @Override
    public void close() {
        mongoClient.close();
    }
}
