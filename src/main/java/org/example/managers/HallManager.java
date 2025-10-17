package org.example.managers;

import jakarta.persistence.EntityManager;
import org.example.model.Hall;
import org.example.repositories.Repository;

import java.util.List;
import java.util.UUID;

public class HallManager {
    private final Repository<Hall> hallRepository;
    private final EntityManager em;

    public HallManager(EntityManager em) {
        this.em = em;
        this.hallRepository = new Repository<Hall>(Hall.class, this.em);
    }

  public Hall createHall(String name, int seatsColumn, int seatsRow) {
        Hall newHall = new Hall(name, seatsColumn, seatsRow);
        return hallRepository.add(newHall);
    }

    public Hall getHallById(UUID id) {
        return hallRepository.findById(id);
    }

    public List<Hall> getAll() {
        return hallRepository.findAll();
    }

}
