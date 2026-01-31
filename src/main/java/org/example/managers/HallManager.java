package org.example.managers;

import org.bson.types.ObjectId;
import org.example.model.Hall;
import org.example.repositories.AbstractRepository;
import org.example.repositories.HallRepository;

import java.util.List;

public class HallManager implements AutoCloseable {
    private final AbstractRepository<Hall> repository;

    public HallManager(AbstractRepository<Hall> repository) {
        this.repository = repository;
    }

    public Hall createHall(String name, int seatsColumn, int seatsRow) {
        Hall newHall = new Hall(name, seatsColumn, seatsRow);
        newHall.setEntityId(new ObjectId());
        return repository.add(newHall);
    }

    public List<Hall> getAll() {
        return repository.findAll();
    }

    @Override
    public void close() throws Exception {
        repository.close();
    }
}
