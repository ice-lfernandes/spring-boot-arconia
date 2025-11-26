package io.arconia.demo.repository;

import io.arconia.demo.entity.Book;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Book entities stored in PostgreSQL.
 */
@Repository
public interface BookRepository extends ListCrudRepository<Book, Long> {

    /**
     * Find books by author name.
     */
    List<Book> findByAuthor(String author);

    /**
     * Find a book by ISBN.
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Find books published after a certain year.
     */
    List<Book> findByPublishedYearGreaterThan(Integer year);

    /**
     * Find books by title containing a keyword (case-insensitive).
     */
    List<Book> findByTitleContainingIgnoreCase(String keyword);
}
