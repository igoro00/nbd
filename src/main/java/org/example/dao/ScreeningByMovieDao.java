package org.example.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.model.ScreeningByMovie;

import java.util.UUID;

@Dao
public interface ScreeningByMovieDao {
    @Select
    PagingIterable<ScreeningByMovie> getAll();

    @Select
    PagingIterable<ScreeningByMovie> getByMovieId(UUID movieId);

    @Insert
    void add(ScreeningByMovie screening);

    @Update
    void update(ScreeningByMovie screening);

    @Delete
    void delete(ScreeningByMovie screening);
}
