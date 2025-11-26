package io.arconia.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for the Spring Boot Arconia application.
 * Uses Arconia Dev Services to automatically provision test containers.
 */
@SpringBootTest
@ActiveProfiles("test")
class ApplicationTests {

    @Test
    void contextLoads() {
        // Verify that the application context loads successfully
    }
}
