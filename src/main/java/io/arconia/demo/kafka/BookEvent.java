package io.arconia.demo.kafka;

import java.io.Serializable;
import java.time.Instant;

/**
 * Event message representing a book-related event for Kafka.
 */
public class BookEvent implements Serializable {

    private String eventId;
    private String eventType;
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private Instant timestamp;

    public BookEvent() {
    }

    public BookEvent(String eventId, String eventType, Long bookId, String bookTitle, String bookAuthor) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.timestamp = Instant.now();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BookEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", bookId=" + bookId +
                ", bookTitle='" + bookTitle + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
