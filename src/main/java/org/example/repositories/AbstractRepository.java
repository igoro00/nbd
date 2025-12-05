package org.example.repositories;

import org.bson.types.ObjectId;
import org.example.managers.RedisManager;
import org.example.model.AbstractEntity;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractRepository <T extends AbstractEntity> implements AutoCloseable{
    public abstract T add(T entity);

    public abstract List<T> findAll();

    public abstract T findById(ObjectId id);

    public abstract long countAll();

    @Override
    public void close() throws Exception {

    }

    public void dropDatabase() {
    }
}
