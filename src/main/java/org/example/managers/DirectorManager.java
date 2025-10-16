package org.example.managers;

import org.example.Repository;
import org.example.model.persons.Director;

import java.util.Date;
import java.util.List;

public class DirectorManager {
    private final Repository<Director> directorRepository;

    public DirectorManager(Repository<Director> clientRepository) {
        this.directorRepository = clientRepository;
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
}
