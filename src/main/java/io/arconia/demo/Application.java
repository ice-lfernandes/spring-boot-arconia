package io.arconia.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * Main application class for the Spring Boot Arconia example.
 * This application demonstrates integration with PostgreSQL, Redis, Kafka, and OpenTelemetry.
 */
@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("Spring Boot Arconia application started successfully!");
    }

    @Bean
    RouterFunction<ServerResponse> rootRouter() {
        return RouterFunctions.route()
            .GET("/", request -> {
                logger.info("Root endpoint called");
                return ServerResponse.ok().body("Welcome to Spring Boot Arconia Example!");
            })
            .build();
    }
}
