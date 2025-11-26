package io.arconia.demo.service;

import io.arconia.demo.entity.CachedSession;
import io.arconia.demo.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Service class for session caching operations with Redis.
 */
@Service
public class CacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);
    private static final String CACHE_PREFIX = "cache:";

    private final SessionRepository sessionRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public CacheService(SessionRepository sessionRepository, RedisTemplate<String, Object> redisTemplate) {
        this.sessionRepository = sessionRepository;
        this.redisTemplate = redisTemplate;
    }

    public CachedSession createSession(String userId, String username, String data) {
        logger.debug("Creating session for user: {}", userId);
        String sessionId = UUID.randomUUID().toString();
        CachedSession session = new CachedSession(sessionId, userId, username, data);
        return sessionRepository.save(session);
    }

    public Optional<CachedSession> getSession(String sessionId) {
        logger.debug("Getting session: {}", sessionId);
        return sessionRepository.findById(sessionId)
            .map(session -> {
                session.setLastAccessedAt(System.currentTimeMillis());
                return sessionRepository.save(session);
            });
    }

    public List<CachedSession> getSessionsByUserId(String userId) {
        logger.debug("Getting sessions for user: {}", userId);
        return sessionRepository.findByUserId(userId);
    }

    public void deleteSession(String sessionId) {
        logger.debug("Deleting session: {}", sessionId);
        sessionRepository.deleteById(sessionId);
    }

    public List<CachedSession> getAllSessions() {
        logger.debug("Getting all sessions");
        return sessionRepository.findAll();
    }

    public void cacheValue(String key, Object value, long ttlSeconds) {
        logger.debug("Caching value with key: {}", key);
        redisTemplate.opsForValue().set(CACHE_PREFIX + key, value, ttlSeconds, TimeUnit.SECONDS);
    }

    public Object getCachedValue(String key) {
        logger.debug("Getting cached value for key: {}", key);
        return redisTemplate.opsForValue().get(CACHE_PREFIX + key);
    }

    public void deleteCachedValue(String key) {
        logger.debug("Deleting cached value for key: {}", key);
        redisTemplate.delete(CACHE_PREFIX + key);
    }
}
