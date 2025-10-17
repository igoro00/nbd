package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.example.model.persons.Client;

import java.util.UUID;

@Entity
@Table(
    name = "ticket",
    uniqueConstraints={
        @UniqueConstraint(columnNames = {"movie_id", "hall_id", "seat_column", "seat_row"})
    }
)
public class Ticket extends ModelEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @NotNull
    @Column(name = "seat_column", nullable = false)
    private int seatColumn;

    @NotNull
    @Column(name = "seat_row", nullable = false)
    private int seatRow;

    @NotNull
    @Column(name = "price", nullable = false)
    private double price;

    public Ticket(Screening screening, Client client, int seatColumn, int seatRow) {
        this.screening = screening;
        this.client = client;
        this.seatColumn = seatColumn;
        this.seatRow = seatRow;
}

    public int getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(int seatColumn) {
        if (seatColumn < 0 || seatColumn >= this.getScreening().getHall().getColumns()) {
            throw new IllegalArgumentException("Seat column value is outside Hall bounds");
        }

        this.seatColumn = seatColumn;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        if (seatRow < 0 || seatRow >= this.getScreening().getHall().getRows()) {
            throw new IllegalArgumentException("Seat row value is outside Hall bounds");
        }

        this.seatRow = seatRow;
    }

    public Screening getScreening() {
        return this.screening;
    }

    public void setScreening(Screening screening, int seatRow, int seatColumn) {
        this.screening = screening;
        this.setSeatRow(seatRow);
        this.setSeatColumn(seatColumn);
    }

    public double getPrice() {
        return price;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
