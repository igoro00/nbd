package org.example.benchmark;

import org.bson.types.ObjectId;
import org.example.managers.RedisManager;
import org.example.model.Client;
import org.example.repositories.ClientRepository;
import org.example.repositories.cache.ClientRepositoryCacheDecorator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(value = 1)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 5, time = 5)
@State(Scope.Benchmark)
public class ClientCacheBenchmark {
    private RedisManager redisManager;
    private ClientRepositoryCacheDecorator cacheDecorator;
    private ObjectId testId;

    @Setup(Level.Trial)
    public void setup() {
        redisManager = new RedisManager();
        ClientRepository mongoRepo = new ClientRepository();
        cacheDecorator = new ClientRepositoryCacheDecorator(mongoRepo, redisManager);
        // Przygotuj dane testowe
        Client testClient = new Client("Benchmark", "Client", "bench@test.pl", new Date(), null);
        mongoRepo.add(testClient);
        testId = testClient.getEntityId();
        // Czyszczenie cache
        try (Jedis jedis = redisManager.getResource()) {
            jedis.flushAll();
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        if (redisManager != null) redisManager.close();
        // Jeśli repozytorium wymaga zamknięcia, dodaj odpowiedni kod
    }

    @Benchmark
    public void test_CacheMiss(Blackhole bh) {
        cacheDecorator.invalidateOne(testId); // unieważnienie cache
        Client client = cacheDecorator.findById(testId);
        bh.consume(client);
    }

    @Benchmark
    public void test_CacheHit(Blackhole bh) {
        cacheDecorator.findById(testId); // załadowanie do cache
        Client client = cacheDecorator.findById(testId); // odczyt z cache
        bh.consume(client);
    }
}