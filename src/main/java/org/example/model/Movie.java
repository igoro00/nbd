package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.example.model.persons.Director;

import java.time.Duration;
import io.hypersistence.utils.hibernate.type.interval.PostgreSQLIntervalType;
import org.hibernate.annotations.Type;


@Entity
@Table(name = "movie")
public class Movie extends ModelEntity{
    @Column(name = "title")
    private String title;

    @Type(PostgreSQLIntervalType.class)
    @Column(name = "time_duration", columnDefinition = "interval")
    private Duration timeDuration;

    @Column(name = "category")
    private String category;

    @Column(name = "basic_price")
    private double basicPrice;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "director_id", nullable = false)
    private Director director;

    @Column(name = "archived")
    private boolean archived;

    public Movie(String title, Duration timeDuration, String category, double basicPrice, Director director) {
        this.title = title;
        this.timeDuration = timeDuration;
        this.category = category;
        this.basicPrice = basicPrice;
        this.director = director;
        this.archived = false;
    }

    public Movie() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Duration getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(Duration timeDuration) {
        this.timeDuration = timeDuration;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(double basicPrice) {
        this.basicPrice = basicPrice;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
