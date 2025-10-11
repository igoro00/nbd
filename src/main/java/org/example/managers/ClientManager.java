package org.example.managers;

import org.example.Repository;
import org.example.model.persons.Client;

import java.util.Date;

public class ClientManager {
    private final Repository<Client> clientRepository;

    // Konstruktor z repozytorium
    public ClientManager(Repository<Client> clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Rejestracja nowego klienta
    public Client registerClient(String firstName, String lastName, Date dateOfBirth, String email) {
        Client newClient = new Client(firstName, lastName, dateOfBirth, email);
        clientRepository.add(newClient);
        return newClient;
    }

    // Wyrejestrowanie klienta
    public void unregisterClient(Client client) {
        client.archive();
    }

    // Pobranie klienta na podstawie adresu email
    public Client getClient(String email) {
        return clientRepository.find(client -> client.getEmail().equals(email));
    }

    // Pobranie raportu wszystkich klientów
    public String getAllClientsReport() {
        return clientRepository.report();
    }
}
