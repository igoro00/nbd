package org.example.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.model.Client;

import java.util.UUID;

@Dao
public interface ClientDao {
    @Select
    PagingIterable<Client> getByClientId(UUID clientId);

    @Select
    PagingIterable<Client> getAll();

    @Insert
    void add(Client client);

    @Update
    void update(Client client);

    @Delete
    void delete(Client client);
}
