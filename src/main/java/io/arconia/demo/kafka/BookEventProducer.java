package io.arconia.demo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Kafka producer service for sending book events.
 */
@Service
public class BookEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(BookEventProducer.class);
    private static final String TOPIC = "book-events";

    private final KafkaTemplate<String, BookEvent> kafkaTemplate;

    public BookEventProducer(KafkaTemplate<String, BookEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBookCreatedEvent(Long bookId, String title, String author) {
        BookEvent event = new BookEvent(
            UUID.randomUUID().toString(),
            "BOOK_CREATED",
            bookId,
            title,
            author
        );
        sendEvent(event);
    }

    public void sendBookUpdatedEvent(Long bookId, String title, String author) {
        BookEvent event = new BookEvent(
            UUID.randomUUID().toString(),
            "BOOK_UPDATED",
            bookId,
            title,
            author
        );
        sendEvent(event);
    }

    public void sendBookDeletedEvent(Long bookId, String title, String author) {
        BookEvent event = new BookEvent(
            UUID.randomUUID().toString(),
            "BOOK_DELETED",
            bookId,
            title,
            author
        );
        sendEvent(event);
    }

    private void sendEvent(BookEvent event) {
        logger.info("Sending book event to Kafka: {}", event);
        kafkaTemplate.send(TOPIC, event.getEventId(), event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Book event sent successfully: topic={}, partition={}, offset={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                } else {
                    logger.error("Failed to send book event: {}", event, ex);
                }
            });
    }
}
