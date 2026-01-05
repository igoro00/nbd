package org.example.repositories;

import lombok.Getter;
import org.example.dao.ClientDao;
import org.example.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientRepository extends AbstractCassandraRepository<Client> {
    private final ClientDao dao;

    public ClientRepository() {
        super();
        this.dao = getMapper().clientDao();
    }

    @Override
    public void delete(Client obj) {
        this.dao.delete(obj);
    }

    @Override
    public void update(Client obj) {
        this.dao.update(obj);
    }

    @Override
    public List<Client> getAll() {
        return this.dao.getAll().all();
    }

    public Client getById(UUID id){
        return this.dao.getByClientId(id).one();
    }

    @Override
    public void add(Client obj) {
        this.dao.add(obj);
    }

}
