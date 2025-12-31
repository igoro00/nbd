package org.example.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import org.example.model.ScreeningByMovie;

import java.util.UUID;

@Dao
public interface ScreeningByMovieDao {
    @Select
    PagingIterable<ScreeningByMovie> getAllScreenings();

    @Select
    PagingIterable<ScreeningByMovie> getScreeningsForMovie(UUID movieId);

    @Insert
    void save(ScreeningByMovie screening);
}
