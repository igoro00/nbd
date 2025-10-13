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


    public Hall(String hallNumber, int seatsColumn, int seatsRow) {
        this.hallNumber = hallNumber;
        this.seatsColumn = seatsColumn;
        this.seatsRow = seatsRow;
    }

    public Hall() {

    }

    public String getHallNumber() {
        return hallNumber;
    }

    public void setHallNumber(String hallNumber) {
        this.hallNumber = hallNumber;
    }

    public int getSeatsColumn() {
        return seatsColumn;
    }

    public void setSeatsColumn(int seatsColumn) {
        this.seatsColumn = seatsColumn;
    }

    public int getSeatsRow() {
        return seatsRow;
    }

    public void setSeatsRow(int seatsRow) {
        this.seatsRow = seatsRow;
    }
}
