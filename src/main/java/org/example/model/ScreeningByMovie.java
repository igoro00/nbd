package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@CqlName("screenings_by_movie")
public class ScreeningByMovie {
    @PartitionKey
    @CqlName("movie_id")
    private UUID movieId;

    @ClusteringColumn(0)
    @CqlName("screening_id")
    private UUID screeningId;

    // Duplicate of Movie.name
    @CqlName("movie_title")
    private String movieTitle;

    @CqlName("hall_name")
    private String hallName;

    @CqlName("start_date")
    private Date startDate;

    // Duplicate of Movie.duration
    @CqlName("movie_duration")
    private Duration movieDuration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;

        ScreeningByMovie screeningByMovie = (ScreeningByMovie) o;

        return new EqualsBuilder().append(this.movieId, screeningByMovie.movieId).append(this.movieName, screeningByMovie.movieName).append(this.hallName, screeningByMovie.hallName).append(this.startDate, screeningByMovie.startDate).append(this.movieDuration, screeningByMovie.movieDuration).isEquals();
    }
}
