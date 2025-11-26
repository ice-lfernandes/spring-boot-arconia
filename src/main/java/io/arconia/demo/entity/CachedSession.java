package io.arconia.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

/**
 * Redis Hash entity for caching session data.
 * This demonstrates Spring Data Redis integration.
 */
@RedisHash(value = "sessions", timeToLive = 3600)
public class CachedSession implements Serializable {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String username;

    private String data;

    private long createdAt;

    private long lastAccessedAt;

    public CachedSession() {
    }

    public CachedSession(String id, String userId, String username, String data) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.data = data;
        this.createdAt = System.currentTimeMillis();
        this.lastAccessedAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(long lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
}
