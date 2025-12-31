package org.example.repositories;

import lombok.Getter;
import org.example.dao.ClientDao;
import org.example.model.Client;

import java.util.List;

public class ClientRepository extends AbstractCassandraRepository<Client> {
    @Getter
    private final ClientDao dao;

    public ClientRepository() {
        super();
        this.dao = getMapper().clientDao();
    }

    @Override
    public void delete(Client obj) {
        getDao().delete(obj);
    }

    @Override
    public void update(Client obj) {
        getDao().update(obj);
    }

    @Override
    public List<Client> getAll() {
        return getDao().getAll().all();
    }

    @Override
    public void add(Client obj) {
        getDao().add(obj);
    }

}
