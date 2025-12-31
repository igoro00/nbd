package org.example.repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import lombok.Getter;
import org.example.EntityMapper;
import org.example.EntityMapperBuilder;

import java.net.InetSocketAddress;
import java.util.List;

public abstract class AbstractCassandraRepository<T> implements AutoCloseable {
    private final CqlSession session;

    @Getter
    private final EntityMapper mapper;

    public AbstractCassandraRepository(){
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .addContactPoint(new InetSocketAddress("cassandra3", 9044))
                .withLocalDatacenter("dc1")
                .withAuthCredentials("cassandra", "cassandrapassword")
                .withKeyspace(CqlIdentifier.fromCql("absolute_cinema"))
                .build();
        mapper = new EntityMapperBuilder(session).build();
    }
    @Override
    public void close(){
        session.close();
    }

    public abstract void delete(T obj);

    public abstract void update(T obj);

    public abstract List<T> getAll();

    public abstract void add(T obj);
}
