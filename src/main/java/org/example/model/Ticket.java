package org.example.model;

import jakarta.persistence.*;
import org.example.model.persons.Client;

import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket extends ModelEntity {
    @Column(name = "seat_column", nullable = false)
    private int seatColumn;

    @Column(name = "seat_row", nullable = false)
    private int seatRow;

    @Column(name = "hall_number", nullable = false)
    private String hallNumber;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    public Ticket(int seatNumber, int seatRow, String hallNumber, Client client, Movie movie) {
        this.seatColumn = seatNumber;
        this.seatRow = seatRow;
        this.hallNumber = hallNumber;
        this.client = client;
        this.movie = movie;
        this.active = true;
}

    public int getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(int seatNumber) {
        if (seatNumber > 0) {
            this.seatColumn = seatNumber;
        }
    }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        if (seatRow > 0) {
            this.seatRow = seatRow;
        }
    }

    public String getHallNumber() {
        return hallNumber;
    }

    public void setHallNumber(String hallNumber) {
        if (hallNumber != null && !hallNumber.isEmpty()) {
            this.hallNumber = hallNumber;
        }
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
