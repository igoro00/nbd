package org.example;

import org.example.model.Address;
import org.example.model.Client;
import org.example.repositories.ClientRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try (ClientRepository clientRepo = new ClientRepository()){
            Client c = new Client(
                    "John",
                    "Doe",
                    "agnieszka.duraj@p.lodz.pl",
                    sdf.parse("15-04-1974"),
                    new Address("Lodz", "90-001", "Bałtycka", "44")
            );
            clientRepo.add(c);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}