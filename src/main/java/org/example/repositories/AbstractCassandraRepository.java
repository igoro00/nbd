package org.example.repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import lombok.Getter;
import org.example.EntityMapper;
import org.example.EntityMapperBuilder;

import java.net.InetSocketAddress;

public abstract class AbstractCassandraRepository {
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
}
