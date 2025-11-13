package org.example.managers;

import org.example.repositories.ClientRepository;
import org.example.model.Address;
import org.example.model.Client;

import java.util.Date;
import java.util.List;

public class ClientManager implements AutoCloseable {
    private final ClientRepository repository;

    public ClientManager() {
        this.repository = new ClientRepository();
    }

    public Client registerClient(String firstName, String lastName, String email, Date dateOfBirth, Address address) {
        Client newClient = new Client(firstName, lastName, email, dateOfBirth, address);
        return repository.add(newClient);
    }

    public List<Client> getAll(){
        return repository.findAll();
    }

    public void deleteAllClients() {
        repository.deleteAll();
    }

    public ClientRepository getRepository() {
        return repository;
    }

    @Override
    public void close() throws Exception {
        repository.close();
    }
}