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

    @Column(name = "hall_number", nullable = false, unique = true)
    private String hallNumber;

    @Column(name = "seats_column", nullable = false)
    private int seatsColumn;

    @Column(name = "seats_row", nullable = false)
    private int seatsRow;


}
