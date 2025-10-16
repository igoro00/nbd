package org.example.model;

import jakarta.persistence.*;
import org.example.model.persons.Director;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class Movie extends ModelEntity{
    @Column(name = "title")
    private String title;

    @Column(name = "start_show_date")
    private Date startShowDate;

    @Column(name = "time_duration")
    private Duration timeDuration;

    @Column(name = "category")
    private String category;

    @Column(name = "basic_price")
    private double basicPrice;

    @ManyToOne
    @JoinColumn(name = "director_id", nullable = false)
    private Director director;

    @Column(name = "archived")
    private boolean archived;

    public Movie(String title, Date startShowDate, Duration timeDuration, String category, double basicPrice, Director director) {
        this.title = title;
        this.startShowDate = startShowDate;
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

    public Date getStartShowDate() {
        return startShowDate;
    }

    public void setStartShowDate(Date startShowDate) {
        this.startShowDate = startShowDate;
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
