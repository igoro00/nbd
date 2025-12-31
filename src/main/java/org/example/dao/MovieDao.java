package org.example.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;
import org.example.model.Movie;

import java.util.UUID;

@Dao
public interface MovieDao {
    @Select
    PagingIterable<Movie> getMoviesById(UUID movieId);

    @Select
    PagingIterable<Movie> getAllMovies();

    @Insert
    void save(Movie movie);

    @Update
    void update(Movie movie);
}
