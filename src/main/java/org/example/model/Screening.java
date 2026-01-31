package org.example.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Screening extends AbstractEntity {
    @BsonProperty("movie")
    private Movie movie;

    @BsonProperty("hall")
    private Hall hall;

    @BsonProperty("start_date")
    private Date startDate;
}
