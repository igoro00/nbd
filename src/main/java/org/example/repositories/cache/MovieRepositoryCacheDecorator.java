package org.example.repositories.cache;

import org.bson.types.ObjectId;
import org.example.managers.RedisManager;
import org.example.model.Movie;
import org.example.repositories.AbstractRepository;
import org.example.repositories.MovieRepository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

public class MovieRepositoryCacheDecorator extends AbstractRepository<Movie> {

	private final MovieRepository delegate;
	private final RedisManager redisManager;
	private final int ttlSeconds;

	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(java.time.Duration.class, new DurationAdapter())
			.create();

	public MovieRepositoryCacheDecorator(MovieRepository delegate, RedisManager redisManager) {
		this.delegate = delegate;
		this.redisManager = redisManager;
		this.ttlSeconds = redisManager.getMovieTtlSeconds();
	}

	private String keyAll() { return "movies:all"; }
	private String keyCount() { return "movies:count"; }
	private String keyById(ObjectId id) { return "movies:" + id; }

	private void invalidateAll() {
		try (Jedis jedis = redisManager.getResource()) {
			jedis.del(keyAll());
			jedis.del(keyCount());
		}
	}

	private void invalidateOne(ObjectId id) {
		try (Jedis jedis = redisManager.getResource()) {
			jedis.del(keyById(id));
		}
	}

	@Override
	public Movie add(Movie movie) {
		delegate.add(movie);

		invalidateAll();
		invalidateOne(movie.getEntityId());

		return movie;
	}

	@Override
	public List<Movie> findAll() {
		String key = keyAll();

		try (Jedis jedis = redisManager.getResource()) {
			String json = jedis.get(key);
			if (json != null) {
				Movie[] arr = gson.fromJson(json, Movie[].class);
				return Arrays.asList(arr);
			}
		} catch (JedisConnectionException e) {
			return delegate.findAll();
		}

		List<Movie> list = delegate.findAll();

		try (Jedis jedis = redisManager.getResource()) {
			jedis.setex(key, ttlSeconds, gson.toJson(list));
		}

		return list;
	}

	@Override
	public Movie findById(ObjectId id) {
		String key = keyById(id);

		try (Jedis jedis = redisManager.getResource()) {
			String json = jedis.get(key);
			if (json != null) {
				return gson.fromJson(json, Movie.class);
			}
		} catch (JedisConnectionException e) {
			return delegate.findById(id);
		}

		Movie movie = delegate.findById(id);
		if (movie != null) {
			try (Jedis jedis = redisManager.getResource()) {
				jedis.setex(key, ttlSeconds, gson.toJson(movie));
			}
		}

		return movie;
	}

	@Override
	public long countAll() {
		String key = keyCount();

		try (Jedis jedis = redisManager.getResource()) {
			String cache = jedis.get(key);
			if (cache != null) {
				return Long.parseLong(cache);
			}
		} catch (JedisConnectionException e) {
			return delegate.countAll();
		}

		long count = delegate.countAll();

		try (Jedis jedis = redisManager.getResource()) {
			jedis.setex(key, ttlSeconds, String.valueOf(count));
		}

		return count;
	}
}
