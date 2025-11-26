package io.arconia.demo.repository;

import io.arconia.demo.entity.CachedSession;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data Redis Repository for CachedSession entities.
 */
@Repository
public interface SessionRepository extends ListCrudRepository<CachedSession, String> {

    /**
     * Find sessions by user ID.
     */
    List<CachedSession> findByUserId(String userId);
}
