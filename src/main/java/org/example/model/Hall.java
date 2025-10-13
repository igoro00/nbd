package org.example.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "halls")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;


}
