package org.example.model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;

public class Screening extends AbstractEntity {
    @BsonProperty("movie")
    private Movie movie;

    @BsonProperty("hall")
    private Hall hall;

    @BsonProperty("start_date")
    private Date startDate;

    @BsonCreator
    public Screening(
            @BsonProperty("_id") ObjectId entityId,
            @BsonProperty("movie") Movie movie,
            @BsonProperty("hall") Hall hall,
            @BsonProperty("start_data") Date startDate) {
        super(entityId);
        this.hall = hall;
        this.movie = movie;
        this.startDate = startDate;
    }

    public Screening(Movie movie, Hall hall, Date startDate) {
        super(new ObjectId());
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
