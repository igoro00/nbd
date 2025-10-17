package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "screening")
public class Screening extends ModelEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name="movie_id", nullable = false)
    private Movie movie;

    @NotNull
    @ManyToOne
    @JoinColumn(name="hall_id", nullable = false)
    private Hall hall;

    @NotNull
    @Column(name="start_date", nullable = false)
    private Date startDate;

    public Screening(Movie movie, Hall hall, Date startDate) {
        this.hall = hall;
        this.movie = movie;
        this.startDate = startDate;
    }

    public Movie getMovie() {
        return movie;
    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    public Hall getHall() {
        return hall;
    }
    public void setHall(Hall hall) {
        this.hall = hall;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
