package org.example.managers;

import jakarta.persistence.EntityManager;
import org.example.repositories.Repository;
import org.example.model.Director;

import java.util.List;

public class DirectorManager {
    private final Repository<Director> directorRepository;

    public DirectorManager(EntityManager em) {
        this.directorRepository = new Repository<Director>(Director.class, em);
    }

    public Director registerDirector(String firstName, String lastName) {
        Director newDirector = new Director(firstName, lastName);
        return directorRepository.add(newDirector);
    }

    public void unregisterClient(Director director) {
        director.archive();
    }

    public List<Director> getAll(){
        return directorRepository.findAll();
    }

    public int getDirectorCount() {
        return directorRepository.countAll();
    }
}
