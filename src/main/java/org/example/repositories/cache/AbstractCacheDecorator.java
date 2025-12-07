package org.example.repositories.cache;

import org.bson.types.ObjectId;
import org.example.model.AbstractEntity;
import org.example.repositories.AbstractRepository;

public abstract class AbstractCacheDecorator<T extends AbstractEntity> extends AbstractRepository<T> {
    abstract String keyAll();
    abstract String keyCount();
    abstract String keyById(ObjectId id);

    public abstract void invalidateOne(ObjectId id);

    public abstract void invalidateAll();
}
