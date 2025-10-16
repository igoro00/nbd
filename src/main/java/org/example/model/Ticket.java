package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.example.model.persons.Client;

import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket extends ModelEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @NotNull
    @Column(name = "seat_column", nullable = false)
    private int seatColumn;

    @NotNull
    @Column(name = "seat_row", nullable = false)
    private int seatRow;

    @NotNull
    @Column(name = "price", nullable = false)
    private double price;

    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active = true;

    public Ticket(Movie movie, Client client, Hall hall, int seatColumn, int seatRow) {
        this.movie = movie;
        this.client = client;
        this.hall = hall;
        this.seatColumn = seatColumn;
        this.seatRow = seatRow;
}

    public int getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(int seatColumn) {
        if (seatColumn < 0 || seatColumn >= this.hall.getColumns()) {
            throw new IllegalArgumentException("Seat column value is outside Hall bounds");
        }

        this.seatColumn = seatColumn;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        if (seatRow < 0 || seatRow >= this.hall.getRows()) {
            throw new IllegalArgumentException("Seat row value is outside Hall bounds");
        }

        this.seatRow = seatRow;
    }

    public Hall getHall() {
        return this.hall;
    }

    public void setHall(Hall hall, int seatRow, int seatColumn) {
        this.hall = hall;
        this.setSeatRow(seatRow);
        this.setSeatColumn(seatColumn);
    }

    public double getPrice() {
        return price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

}
