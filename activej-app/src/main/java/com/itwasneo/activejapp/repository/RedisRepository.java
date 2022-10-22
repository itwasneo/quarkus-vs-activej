package com.itwasneo.activejapp.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPooled;

import java.util.Optional;

public class RedisRepository<T> {

	private static final Logger logger = LoggerFactory.getLogger(RedisRepository.class);
	private static final String KEY_DELIMITER = ":";
	private final JedisPooled redisPool;
	private final ObjectMapper objectMapper;

	public RedisRepository(JedisPooled redisPool, ObjectMapper objectMapper) {
		this.redisPool = redisPool;
		this.objectMapper = objectMapper;
	}

	public Optional<String> findByKeys(Class<T> type, String... keys) {
		try {
			String redisResult = redisPool.get(createRedisKey(keys));
			if (redisResult == null || redisResult.isEmpty()) {
				return Optional.empty();
			}
			objectMapper.readValue(redisResult, type);
			return Optional.of(redisResult);
		} catch (JsonProcessingException jpe) {
			logger.error("", jpe);
			return Optional.empty();
		}
	}

	public boolean save(T obj, String... keys) {
		try {
			redisPool.set(createRedisKey(keys), objectMapper.writeValueAsString(obj));
			return true;
		} catch (JsonProcessingException jpe) {
			logger.error("", jpe);
			return false;
		}
	}

	private String createRedisKey(String... keys) {
		return String.join(KEY_DELIMITER, keys);
	}

}
