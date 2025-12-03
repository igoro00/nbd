package org.example.repositories.cache;

import org.bson.types.ObjectId;
import org.example.managers.RedisManager;
import org.example.model.Hall;
import org.example.repositories.AbstractRepository;
import org.example.repositories.HallRepository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.json.DefaultGsonObjectMapper;

import java.util.Arrays;
import java.util.List;

public class HallRepositoryCacheDecorator extends AbstractRepository<Hall> {

	private final HallRepository delegate;
	private final RedisManager redisManager;
	private final int ttlSeconds;
	private final DefaultGsonObjectMapper mapper = new DefaultGsonObjectMapper();

	public HallRepositoryCacheDecorator(HallRepository delegate, RedisManager redisManager) {
		this.delegate = delegate;
		this.redisManager = redisManager;
		this.ttlSeconds = redisManager.getHallTtlSeconds(); // Dodaj tę metodę w RedisManager lub ustaw na sztywno
	}

	private String keyAll() {
		return "halls:all";
	}

	private String keyCount() {
		return "halls:count";
	}

	public Hall add(Hall hall) {
		delegate.add(hall);
		try (Jedis jedis = redisManager.getResource()) {
			jedis.del(keyAll());
			jedis.del(keyCount());
		} catch (JedisConnectionException e) {
		}
		invalidateCache(hall.getEntityId());
		return hall;
	}

	@Override
	public List<Hall> findAll() {
		String key = keyAll();
		try (Jedis jedis = redisManager.getResource()) {
			String json = jedis.get(key);
			if (json != null) {
				Hall[] arr = mapper.fromJson(json, Hall[].class);
				return Arrays.asList(arr);
			}
		} catch (JedisConnectionException e) {
			return delegate.findAll();
		}

		List<Hall> all = delegate.findAll();
		try (Jedis jedis = redisManager.getResource()) {
			jedis.setex(key, ttlSeconds, mapper.toJson(all));
		} catch (JedisConnectionException e) {
		}
		return all;
	}

	@Override
	public Hall findById(ObjectId id) {
		String key = "halls:" + id;
		try (Jedis jedis = redisManager.getResource()) {
			String json = jedis.get(key);
			if (json != null) {
				return mapper.fromJson(json, Hall.class);
			}
		} catch (JedisConnectionException e) {
			return delegate.findById(id);
		}

		Hall hall = delegate.findById(id);
		if (hall != null) {
			try (Jedis jedis = redisManager.getResource()) {
				jedis.setex(key, ttlSeconds, mapper.toJson(hall));
			} catch (JedisConnectionException e) {
			}
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
