package org.example.managers;

import org.example.model.Hall;
import org.example.repositories.HallRepository;

import java.util.List;

public class HallManager implements AutoCloseable {
    private final HallRepository repository;

    public HallManager() {
        this.repository = new HallRepository();
    }

    public Hall createHall(String name, int seatsColumn, int seatsRow) {
        Hall newHall = new Hall(name, seatsColumn, seatsRow);
        return repository.add(newHall);
    }

    public List<Hall> getAll() {
        return repository.findAll();
    }

    public void deleteAllHalls() {
        repository.deleteAll();
    }

    @Override
    public void close() throws Exception {
        repository.close();
    }
}
