package org.example.repositories.cache;

import org.bson.types.ObjectId;
import org.example.managers.RedisManager;
import org.example.model.Movie;
import org.example.repositories.AbstractRepository;
import org.example.repositories.MovieRepository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.json.DefaultGsonObjectMapper;

import java.util.Arrays;
import java.util.List;

public class MovieRepositoryCacheDecorator extends AbstractRepository<Movie> {

	private final MovieRepository delegate;
	private final RedisManager redisManager;
	private final int ttlSeconds;
	private final DefaultGsonObjectMapper mapper = new DefaultGsonObjectMapper();

	public MovieRepositoryCacheDecorator(MovieRepository delegate, RedisManager redisManager) {
		this.delegate = delegate;
		this.redisManager = redisManager;
		this.ttlSeconds = redisManager.getMovieTtlSeconds();
	}

	private String keyAll() {
		return "movies:all";
	}

	private String keyCount() {
		return "movies:count";
	}

	public Movie add(Movie movie) {
		delegate.add(movie);
		try (Jedis jedis = redisManager.getResource()) {
			jedis.del(keyAll());
			jedis.del(keyCount());
		} catch (JedisConnectionException e) {
		}
		invalidateCache(movie.getEntityId());
		return movie;
	}

	@Override
	public List<Movie> findAll() {
		String key = keyAll();
		try (Jedis jedis = redisManager.getResource()) {
			String json = jedis.get(key);
			if (json != null) {
				Movie[] arr = mapper.fromJson(json, Movie[].class);
				return Arrays.asList(arr);
			}
		} catch (JedisConnectionException e) {
			return delegate.findAll();
		}

		List<Movie> all = delegate.findAll();
		try (Jedis jedis = redisManager.getResource()) {
			jedis.setex(key, ttlSeconds, mapper.toJson(all));
		} catch (JedisConnectionException e) {
		}
		return all;
	}

	@Override
	public Movie findById(ObjectId id) {
		String key = "movies:" + id;
		try (Jedis jedis = redisManager.getResource()) {
			String json = jedis.get(key);
			if (json != null) {
				return mapper.fromJson(json, Movie.class);
			}
		} catch (JedisConnectionException e) {
			return delegate.findById(id);
		}

		Movie movie = delegate.findById(id);
		if (movie != null) {
			try (Jedis jedis = redisManager.getResource()) {
				jedis.setex(key, ttlSeconds, mapper.toJson(movie));
			} catch (JedisConnectionException e) {
			}
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
		} catch (JedisConnectionException e) {
		}
		return count;
	}

	public void invalidateCache(ObjectId id) {
		String key = "clients:" + id;
		try (Jedis jedis = redisManager.getResource()) {
			jedis.del(key);
		} catch (JedisConnectionException e) {
		}
	}
}
