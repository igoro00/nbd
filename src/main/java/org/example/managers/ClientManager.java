package org.example.managers;

import org.example.UUID7;
import org.example.model.Client;
import org.example.repositories.ClientRepository;

import java.util.Date;
import java.util.List;

public record ClientManager(ClientRepository repository) {
    public Client registerClient(String firstName, String lastName, String email, Date dateOfBirth) {
        Client newClient = new Client(UUID7.randomUUID(), firstName, lastName, email, dateOfBirth);
        repository.add(newClient);
        return newClient;
    }

    public List<Client> getAll() {
        return repository.getAll();
    }

}