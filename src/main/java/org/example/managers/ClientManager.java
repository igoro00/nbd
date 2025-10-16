package org.example.managers;

import org.example.Repository;
import org.example.model.persons.Client;

import java.util.Date;
import java.util.List;

public class ClientManager {
    private final Repository<Client> clientRepository;

    public ClientManager(Repository<Client> clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client registerClient(String firstName, String lastName, Date dateOfBirth, String email) {
        Client newClient = new Client(firstName, lastName, dateOfBirth, email);
        return clientRepository.add(newClient);
    }

    public void unregisterClient(Client client) {
        client.archive();
    }

    public List<Client> getAll(){
        return clientRepository.findAll();
    }
}
