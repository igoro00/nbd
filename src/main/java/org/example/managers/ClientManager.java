package org.example.managers;

import org.example.repositories.ClientRepository;
import org.example.model.Address;
import org.example.model.Client;

import java.util.Date;
import java.util.List;

public class ClientManager {
    private final ClientRepository clientRepository;

    public ClientManager(ClientRepository repo) {
        this.clientRepository = repo;
    }

    public Client registerClient(String firstName, String lastName, String email, Date dateOfBirth, Address address) {
        Client newClient = new Client(firstName, lastName, email, dateOfBirth, address);
        return clientRepository.add(newClient);
    }

//    public void unregisterClient(Client client) {
//        client.archive();
//    }
//
    public List<Client> getAll(){
        return clientRepository.findAll();
    }

    public long getClientCount() {
        return clientRepository.countAll();
    }
}