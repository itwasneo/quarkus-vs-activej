package com.itwasneo.quarkusapp.repository;

import io.quarkus.redis.datasource.string.StringCommands;
import io.quarkus.redis.datasource.RedisDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class RedisRepository<T> {

    private static final String KEY_DELIMITER = ":";
    
    private final StringCommands<String, String> commands;
    private final ObjectMapper objectMapper;
   
    public RedisRepository(
        RedisDataSource redisDataSource,
        ObjectMapper objectMapper) {

        this.commands = redisDataSource.string(String.class);
        this.objectMapper = objectMapper;

    }
    
    public Optional<T> findByKeys(Class<T> type, String... keys) {
        try {
            String redisResult = commands.get(createRedisKey(keys));
            if (redisResult == null || redisResult.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(redisResult, type));
        } catch (JsonProcessingException jpe) {
            return Optional.empty();
        }
    }
    
    public boolean save(T obj, String... keys) {
        try {
            commands.set(createRedisKey(keys), objectMapper.writeValueAsString(obj));
            return true;
        } catch (JsonProcessingException jpe) {
            return false;
        }
    }
    
    private String createRedisKey(String... keys) {
        return String.join(KEY_DELIMITER, keys);
    }
}