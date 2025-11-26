package io.arconia.demo.controller;

import io.arconia.demo.entity.CachedSession;
import io.arconia.demo.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Cache/Session operations.
 * Demonstrates Redis integration with Spring Data Redis.
 */
@RestController
@RequestMapping("/api/cache")
public class CacheController {

    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<CachedSession>> getAllSessions() {
        logger.info("GET /api/cache/sessions - Fetching all sessions");
        List<CachedSession> sessions = cacheService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<CachedSession> getSession(@PathVariable String sessionId) {
        logger.info("GET /api/cache/sessions/{} - Fetching session", sessionId);
        return cacheService.getSession(sessionId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sessions/user/{userId}")
    public ResponseEntity<List<CachedSession>> getSessionsByUserId(@PathVariable String userId) {
        logger.info("GET /api/cache/sessions/user/{} - Fetching sessions by user", userId);
        List<CachedSession> sessions = cacheService.getSessionsByUserId(userId);
        return ResponseEntity.ok(sessions);
    }

    @PostMapping("/sessions")
    public ResponseEntity<CachedSession> createSession(@RequestBody Map<String, String> request) {
        logger.info("POST /api/cache/sessions - Creating new session");
        String userId = request.get("userId");
        String username = request.get("username");
        String data = request.get("data");
        
        CachedSession session = cacheService.createSession(userId, username, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable String sessionId) {
        logger.info("DELETE /api/cache/sessions/{} - Deleting session", sessionId);
        cacheService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/values")
    public ResponseEntity<Map<String, String>> cacheValue(@RequestBody Map<String, Object> request) {
        logger.info("POST /api/cache/values - Caching value");
        String key = (String) request.get("key");
        Object value = request.get("value");
        long ttl = request.containsKey("ttl") ? ((Number) request.get("ttl")).longValue() : 3600L;
        
        cacheService.cacheValue(key, value, ttl);
        return ResponseEntity.ok(Map.of("status", "cached", "key", key));
    }

    @GetMapping("/values/{key}")
    public ResponseEntity<Object> getCachedValue(@PathVariable String key) {
        logger.info("GET /api/cache/values/{} - Getting cached value", key);
        Object value = cacheService.getCachedValue(key);
        if (value != null) {
            return ResponseEntity.ok(value);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/values/{key}")
    public ResponseEntity<Void> deleteCachedValue(@PathVariable String key) {
        logger.info("DELETE /api/cache/values/{} - Deleting cached value", key);
        cacheService.deleteCachedValue(key);
        return ResponseEntity.noContent().build();
    }
}
