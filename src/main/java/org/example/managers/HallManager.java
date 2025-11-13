package org.example.managers;

import org.example.model.Hall;

import java.util.List;
import java.util.UUID;

public class HallManager {
    private final Repository<Hall> hallRepository;

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

    public int getHallCount() {
        return hallRepository.countAll();
    }
}
