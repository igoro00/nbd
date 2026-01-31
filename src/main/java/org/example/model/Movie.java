package org.example.model;

import java.time.Duration;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
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
}
