package org.example.managers;

import org.bson.types.ObjectId;
import org.example.model.Hall;
import org.example.model.Movie;
import org.example.model.Screening;
import org.example.repositories.ScreeningRepository;

import java.util.Date;
import java.util.List;

public class ScreeningManager implements AutoCloseable {
    private final ScreeningRepository repository;

    public ScreeningManager(ScreeningRepository repository) {
        this.repository = repository;
    }

    public long getScreeningCount() {
        return repository.countAll();
    }

    public List<Screening> getAll() {
        return repository.findAll();
    }

    public Screening createScreening(Movie movie, Hall hall, Date screeningDate) {
        Screening screening = new Screening(movie, hall, screeningDate);
        screening.setEntityId(new ObjectId());
        return repository.add(screening);
    }

    @Override
    public void close() throws Exception {
        repository.close();
    }
}
