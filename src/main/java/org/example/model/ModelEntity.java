package org.example.model;

import jakarta.persistence.*;

import java.util.UUID;

@MappedSuperclass
public abstract class ModelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    public UUID getId() {
        return id;
    }
}
