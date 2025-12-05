package org.example.repositories.cache;

import org.bson.types.ObjectId;
import org.example.managers.RedisManager;
import org.example.model.Hall;
import org.example.repositories.AbstractRepository;
import org.example.repositories.HallRepository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

public class HallRepositoryCacheDecorator extends AbstractRepository<Hall> {

	private final HallRepository delegate;
	private final RedisManager redisManager;
	private final int ttlSeconds;

	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(java.time.Duration.class, new DurationAdapter())
			.create();

	public HallRepositoryCacheDecorator(HallRepository delegate, RedisManager redisManager) {
		this.delegate = delegate;
		this.redisManager = redisManager;
		this.ttlSeconds = redisManager.getHallTtlSeconds();
	}

	private String keyAll() {return "halls:all";}
	private String keyCount() {return "halls:count";}
	private String keyId(ObjectId id) {return "halls:" + id;}

	public void invalidateCache(ObjectId id) {
		try (Jedis jedis = redisManager.getResource()) {
			jedis.del(keyId(id));
		} catch (JedisConnectionException ignored) {}
	}

	public void invalidateAll() {
		try (Jedis jedis = redisManager.getResource()) {
			jedis.del(keyAll());
			jedis.del(keyCount());
		} catch (JedisConnectionException ignored) {}
	}

	@Override
	public Hall add(Hall hall) {
		delegate.add(hall);
		invalidateAll();
		invalidateCache(hall.getEntityId());
		return hall;
	}

	@Override
	public List<Hall> findAll() {
		String key = keyAll();

		try (Jedis jedis = redisManager.getResource()) {
			String json = jedis.get(key);
			if (json != null) {
				return Arrays.asList(gson.fromJson(json, Hall[].class));
			}
		} catch (JedisConnectionException e) {
			return delegate.findAll();
		}

		List<Hall> all = delegate.findAll();

		try (Jedis jedis = redisManager.getResource()) {
			jedis.setex(key, ttlSeconds, gson.toJson(all));
		} catch (JedisConnectionException ignored) {}

		return all;
	}

	@Override
	public Hall findById(ObjectId id) {
		String key = keyId(id);

		try (Jedis jedis = redisManager.getResource()) {
			String json = jedis.get(key);
			if (json != null) {
				return gson.fromJson(json, Hall.class);
			}
		} catch (JedisConnectionException e) {
			return delegate.findById(id);
		}

		Hall hall = delegate.findById(id);

		if (hall != null) {
			try (Jedis jedis = redisManager.getResource()) {
				jedis.setex(key, ttlSeconds, gson.toJson(hall));
			} catch (JedisConnectionException ignored) {}
		}

		return hall;
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
		} catch (JedisConnectionException ignored) {}

		return count;
	}
}
