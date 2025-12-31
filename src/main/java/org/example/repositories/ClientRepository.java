package org.example.repositories;

import lombok.Getter;
import org.example.dao.ClientDao;

public class ClientRepository extends AbstractCassandraRepository {
    @Getter
    private final ClientDao dao;

    public ClientRepository() {
        super();
        this.dao = getMapper().clientDao();
    }
}
