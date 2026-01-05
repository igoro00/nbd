package org.example.managers;

import org.example.UUID7;
import org.example.model.Client;
import org.example.repositories.ClientRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ClientManager(ClientRepository repository) {
    public Client registerClient(String firstName, String lastName, String email, Instant dateOfBirth) {
        Client newClient = new Client(UUID7.randomUUID(), firstName, lastName, email, dateOfBirth);
        repository.add(newClient);
        return newClient;
    }

    public List<Client> getAll() {
        return repository.getAll();
    }

    public Client getById(UUID id) {
        return repository.getById(id);
    }

    public void delete(Client client) {
        repository.delete(client);
    }

    public void update(Client client) {
        repository.update(client);
    }

}