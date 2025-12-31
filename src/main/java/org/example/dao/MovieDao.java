package org.example.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.model.Movie;

import java.util.UUID;

@Dao
public interface MovieDao {
    @Select
    PagingIterable<Movie> getByMovieId(UUID movieId);

    @Select
    PagingIterable<Movie> getAll();

    @Insert
    void add(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);
}
