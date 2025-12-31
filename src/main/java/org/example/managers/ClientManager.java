package org.example.managers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.UUID7;
import org.example.mappers.AbstractRepository;
import org.example.model.Address;
import org.example.model.Client;
import org.example.repositories.ClientRepository;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ClientManager {
    private final ClientRepository repository;

    public Client registerClient(String firstName, String lastName, String email, Date dateOfBirth) {
        Client newClient = new Client(
                UUID7.randomUUID(),
                firstName,
                lastName,
                email,
                dateOfBirth
        );
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