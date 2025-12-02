package org.example.repositories.cache;

import org.example.managers.RedisManager;
import org.example.model.Client;
import org.example.repositories.ClientRepository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.json.DefaultGsonObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ClientRepositoryCacheDecorator {

    private final ClientRepository delegate;
    private final RedisManager redisManager;
    private final int ttlSeconds;
    private final DefaultGsonObjectMapper mapper = new DefaultGsonObjectMapper();

    public ClientRepositoryCacheDecorator(ClientRepository delegate, RedisManager redisManager) {
        this.delegate = delegate;
        this.redisManager = redisManager;
        this.ttlSeconds = redisManager.getClientTtlSeconds();
    }

    private String keyFor(UUID id) {
        return "client:" + id.toString();
    }

    private String keyAll() {
        return "clients:all";
    }

    public void add(Client client) {
        delegate.add(client);
        try (Jedis jedis = redisManager.getResource()) {
            jedis.del(keyAll());
        } catch (JedisConnectionException e) {
        }
    }

    public List<Client> findAll() {
        String key = keyAll();
        try (Jedis jedis = redisManager.getResource()) {
            String json = jedis.get(key);
            if (json != null) {
                Client[] arr = mapper.fromJson(json, Client[].class);
                return Arrays.asList(arr);
            }
        } catch (JedisConnectionException e) {
            return delegate.findAll();
        }

        List<Client> all = delegate.findAll();
        try (Jedis jedis = redisManager.getResource()) {
            jedis.setex(key, ttlSeconds, mapper.toJson(all));
        } catch (JedisConnectionException e) {
        }
        return all;
    }
}
