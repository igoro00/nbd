package org.example.managers;

import lombok.Getter;
import org.example.mappers.AbstractRepository;
import org.example.model.Address;
import org.example.model.Client;

import java.util.Date;
import java.util.List;

@Getter
public class ClientManager implements AutoCloseable {
    private final AbstractRepository<Client> repository;

    public ClientManager(AbstractRepository<Client> repository) {
        this.repository = repository;
    }
    public Client registerClient(String firstName, String lastName, String email, Date dateOfBirth, Address address) {
        Client newClient = new Client(firstName, lastName, email, dateOfBirth, address);
        return repository.add(newClient);
    }

    public List<Client> getAll(){
        return repository.findAll();
    }


    @Override
    public void close() throws Exception {
        repository.close();
    }
}