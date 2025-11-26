package io.arconia.demo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer service for processing book events.
 */
@Service
public class BookEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(BookEventConsumer.class);

    @KafkaListener(topics = "book-events", groupId = "arconia-consumer-group")
    public void handleBookEvent(BookEvent event) {
        logger.info("Received book event from Kafka: {}", event);
        
        switch (event.getEventType()) {
            case "BOOK_CREATED":
                handleBookCreated(event);
                break;
            case "BOOK_UPDATED":
                handleBookUpdated(event);
                break;
            case "BOOK_DELETED":
                handleBookDeleted(event);
                break;
            default:
                logger.warn("Unknown event type: {}", event.getEventType());
        }
    }

    private void handleBookCreated(BookEvent event) {
        logger.info("Processing BOOK_CREATED event: bookId={}, title={}", 
            event.getBookId(), event.getBookTitle());
    }

    private void handleBookUpdated(BookEvent event) {
        logger.info("Processing BOOK_UPDATED event: bookId={}, title={}", 
            event.getBookId(), event.getBookTitle());
    }

    private void handleBookDeleted(BookEvent event) {
        logger.info("Processing BOOK_DELETED event: bookId={}, title={}", 
            event.getBookId(), event.getBookTitle());
    }
}
