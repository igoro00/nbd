package org.example.model;

import java.time.Duration;
import java.util.UUID;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@CqlName("movies")
@PropertyStrategy(mutable = true)
public class Movie {
    @PartitionKey
    @CqlName("movie_id")
    private UUID movieId;

    @CqlName("title")
    private String title;

    @CqlName("duration")
    private Duration duration;

    @CqlName("category")
    private String category;

    @CqlName("price")
    private double price;

    @CqlName("director_name")
    private String directorName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return new EqualsBuilder()
                .append(this.movieId, movie.movieId)
                .append(this.title, movie.title)
                .append(this.duration, movie.duration)
                .append(this.category, movie.category)
                .append(this.price, movie.price)
                .append(this.directorName, movie.directorName)
                .isEquals();
    }
}
