package org.example.managers;

import jakarta.persistence.EntityManager;
import org.example.repositories.Repository;
import org.example.model.Address;
import org.example.model.persons.Client;

import java.util.Date;
import java.util.List;

public class ClientManager {
    private final Repository<Client> clientRepository;

    public ClientManager(EntityManager em) {
        this.clientRepository = new Repository<Client>(Client.class, em);
    }

    public Client registerClient(String firstName, String lastName, String email, Date dateOfBirth, Address address) {
        Client newClient = new Client(firstName, lastName, email, dateOfBirth, address);
        return clientRepository.add(newClient);
    }

    public void unregisterClient(Client client) {
        client.archive();
    }

    public List<Client> getAll(){
        return clientRepository.findAll();
    }

    public int getClientCount() {
        return clientRepository.countAll();
    }
}
