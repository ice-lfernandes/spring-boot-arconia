package io.arconia.demo.service;

import io.arconia.demo.entity.Book;
import io.arconia.demo.kafka.BookEventProducer;
import io.arconia.demo.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Book operations with PostgreSQL and Kafka integration.
 */
@Service
@Transactional
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;
    private final BookEventProducer bookEventProducer;

    public BookService(BookRepository bookRepository, BookEventProducer bookEventProducer) {
        this.bookRepository = bookRepository;
        this.bookEventProducer = bookEventProducer;
    }

    public List<Book> findAllBooks() {
        logger.debug("Finding all books");
        return bookRepository.findAll();
    }

    public Optional<Book> findBookById(Long id) {
        logger.debug("Finding book by id: {}", id);
        return bookRepository.findById(id);
    }

    public Optional<Book> findBookByIsbn(String isbn) {
        logger.debug("Finding book by ISBN: {}", isbn);
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> findBooksByAuthor(String author) {
        logger.debug("Finding books by author: {}", author);
        return bookRepository.findByAuthor(author);
    }

    public List<Book> searchBooksByTitle(String keyword) {
        logger.debug("Searching books by title keyword: {}", keyword);
        return bookRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public Book createBook(Book book) {
        logger.info("Creating new book: {}", book.getTitle());
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        Book savedBook = bookRepository.save(book);
        
        bookEventProducer.sendBookCreatedEvent(
            savedBook.getId(), 
            savedBook.getTitle(), 
            savedBook.getAuthor()
        );
        
        return savedBook;
    }

    public Optional<Book> updateBook(Long id, Book bookDetails) {
        logger.info("Updating book with id: {}", id);
        return bookRepository.findById(id)
            .map(book -> {
                book.setTitle(bookDetails.getTitle());
                book.setAuthor(bookDetails.getAuthor());
                book.setIsbn(bookDetails.getIsbn());
                book.setPublishedYear(bookDetails.getPublishedYear());
                book.setUpdatedAt(LocalDateTime.now());
                Book updatedBook = bookRepository.save(book);
                
                bookEventProducer.sendBookUpdatedEvent(
                    updatedBook.getId(), 
                    updatedBook.getTitle(), 
                    updatedBook.getAuthor()
                );
                
                return updatedBook;
            });
    }

    public boolean deleteBook(Long id) {
        logger.info("Deleting book with id: {}", id);
        return bookRepository.findById(id)
            .map(book -> {
                bookRepository.delete(book);
                bookEventProducer.sendBookDeletedEvent(
                    book.getId(), 
                    book.getTitle(), 
                    book.getAuthor()
                );
                return true;
            })
            .orElse(false);
    }
}
