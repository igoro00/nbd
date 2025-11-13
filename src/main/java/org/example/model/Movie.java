package org.example.model;

import java.time.Duration;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Movie extends AbstractEntity {
    @BsonProperty("title")
    private String title;

    @BsonProperty("duration")
    private Duration duration;

    @BsonProperty("category")
    private String category;

    @BsonProperty("basic_price")
    private double basicPrice;

    @BsonProperty("director")
    private Director director;

    @BsonCreator
    public Movie(
            @BsonProperty("_id") ObjectId entityId,
            @BsonProperty("title") String title,
            @BsonProperty("duration") Duration duration,
            @BsonProperty("category") String category,
            @BsonProperty("basicPrice") double basicPrice,
            @BsonProperty("director") Director director) {
        super(entityId);
        this.title = title;
        this.duration = duration;
        this.category = category;
        this.basicPrice = basicPrice;
        this.director = director;
    }

    public Movie(
            String title,
            Duration duration,
            String category,
            double basicPrice,
            Director director) {
        super(new ObjectId());
        this.title = title;
        this.duration = duration;
        this.category = category;
        this.basicPrice = basicPrice;
        this.director = director;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (Double.compare(movie.basicPrice, basicPrice) != 0) return false;
        if (!title.equals(movie.title)) return false;
        if (!duration.equals(movie.duration)) return false;
        if (!category.equals(movie.category)) return false;
        if (!director.equals(movie.director)) return false;

        return true;
    }
}
