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

public class MovieRepositoryCacheDecorator extends AbstractCacheDecorator<Movie> {

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

	@Override
	String keyAll() { return "movies:all"; }
	@Override
	String keyCount() { return "movies:count"; }
	@Override
	String keyById(ObjectId id) { return "movies:" + id; }

	@Override
	public void invalidateAll() {
		try (Jedis jedis = redisManager.getResource()) {
			jedis.del(keyAll());
			jedis.del(keyCount());
		} catch (JedisConnectionException ignored) {
			// Ignore if Redis is unavailable
		}
	}

	@Override
	public void invalidateOne(ObjectId id) {
		try (Jedis jedis = redisManager.getResource()) {
			jedis.del(keyById(id));
		} catch (JedisConnectionException ignored) {
			// Ignore if Redis is unavailable
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
				return Arrays.asList(gson.fromJson(json, Movie[].class));
			}
		} catch (JedisConnectionException e) {
			return delegate.findAll();
		}

		List<Movie> all = delegate.findAll();

		try (Jedis jedis = redisManager.getResource()) {
			jedis.setex(key, ttlSeconds, gson.toJson(all));
		} catch (JedisConnectionException ignored) {}

		return all;
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
			} catch (JedisConnectionException ignored) {}
		}

		return movie;
	}

	@Override
	public long countAll() {
		String key = keyCount();

		try (Jedis jedis = redisManager.getResource()) {
			String cached = jedis.get(key);
			if (cached != null) {
				try {
					return Long.parseLong(cached);
				} catch (NumberFormatException ignored) {}
			}
		} catch (JedisConnectionException e) {
			return delegate.countAll();
		}

		long count = delegate.countAll();
		try (Jedis jedis = redisManager.getResource()) {
			jedis.setex(key, ttlSeconds, String.valueOf(count));
		} catch (JedisConnectionException ignored) {
		}
		return count;
	}
}
