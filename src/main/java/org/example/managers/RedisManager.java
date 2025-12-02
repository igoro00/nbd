package org.example.managers;

import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RedisManager {

    private final JedisPool jedisPool;
    @Getter
    private final int clientTtlSeconds;
    @Getter
    private final int facilityTtlSeconds;
    @Getter
    private final int rentTtlSeconds;

    public RedisManager() {
        Properties props = loadProperties();
        String host = props.getProperty("redis.host", "localhost");
        int port = Integer.parseInt(props.getProperty("redis.port", "6379"));
        String password = props.getProperty("redis.password", "");
        int timeout = Integer.parseInt(props.getProperty("redis.timeout", "2000"));
        this.clientTtlSeconds = Integer.parseInt(props.getProperty("redis.client_ttl_seconds", "3600"));
        this.facilityTtlSeconds = Integer.parseInt(props.getProperty("redis.facility_ttl_seconds", "7200"));
        this.rentTtlSeconds = Integer.parseInt(props.getProperty("redis.rent_ttl_seconds", "1800"));

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(50);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(1);

        if (password == null || password.trim().isEmpty()) {
            this.jedisPool = new JedisPool(poolConfig, host, port, timeout);
        } else {
            this.jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
        }
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("redis.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    public Jedis getResource() {
        return jedisPool.getResource();
    }

    public void close() {
        jedisPool.close();
    }
}
