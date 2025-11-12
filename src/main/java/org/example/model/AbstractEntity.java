package org.example.model;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.io.Serializable;

public class AbstractEntity implements Serializable {
    @BsonProperty("_id")
    private final ObjectId entityId;
    public ObjectId getEntityId() {
        return entityId;
    }
    public AbstractEntity(ObjectId entityId) {
        this.entityId = entityId;
    }
}
