package io.arconia.demo.controller;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller demonstrating OpenTelemetry and Micrometer observability features.
 * Showcases both programmatic and declarative approaches to metrics and tracing.
 */
@RestController
@RequestMapping("/api/observability")
public class ObservabilityController {

    private static final Logger logger = LoggerFactory.getLogger(ObservabilityController.class);

    private final ObservationRegistry observationRegistry;
    private final Meter meter;
    private final Tracer tracer;

    public ObservabilityController(ObservationRegistry observationRegistry, Meter meter, Tracer tracer) {
        this.observationRegistry = observationRegistry;
        this.meter = meter;
        this.tracer = tracer;
    }

    /**
     * Demonstrates programmatic Micrometer Observation.
     */
    @GetMapping("/micrometer/programmatic")
    public String micrometerProgrammatic() {
        logger.info("Micrometer programmatic observation endpoint called");
        return Observation.createNotStarted("micrometer.greeting", observationRegistry)
            .lowCardinalityKeyValue("type", "programmatic")
            .lowCardinalityKeyValue("endpoint", "observability")
            .observe(() -> {
                logger.info("Inside Micrometer observation context");
                return "Hello from programmatic Micrometer Observation!";
            });
    }

    /**
     * Demonstrates declarative Micrometer Observation using @Observed annotation.
     */
    @GetMapping("/micrometer/declarative")
    @Observed(name = "micrometer.greeting", lowCardinalityKeyValues = {"type=declarative", "endpoint=observability"})
    public String micrometerDeclarative() {
        logger.info("Micrometer declarative observation endpoint called");
        return "Hello from declarative Micrometer Observation!";
    }

    /**
     * Demonstrates programmatic OpenTelemetry metrics.
     */
    @GetMapping("/otel/metrics")
    public String otelMetrics() {
        logger.info("OpenTelemetry metrics endpoint called");
        
        meter.counterBuilder("arconia.api.requests")
            .setDescription("Number of API requests")
            .build()
            .add(1L, Attributes.builder()
                .put("endpoint", "otel-metrics")
                .put("type", "counter")
                .build());

        meter.gaugeBuilder("arconia.api.gauge")
            .setDescription("Sample gauge metric")
            .build()
            .set(Math.random() * 100, Attributes.builder()
                .put("type", "gauge")
                .build());

        return "Hello from OpenTelemetry Metrics! Counter and gauge recorded.";
    }

    /**
     * Demonstrates programmatic OpenTelemetry tracing.
     */
    @GetMapping("/otel/traces")
    public String otelTraces() {
        logger.info("OpenTelemetry tracing endpoint called");
        
        Span span = tracer.spanBuilder("arconia.custom.span")
            .setAttribute("type", "programmatic")
            .setAttribute("endpoint", "otel-traces")
            .startSpan();
        
        try {
            Span childSpan = tracer.spanBuilder("arconia.custom.child.span")
                .setParent(io.opentelemetry.context.Context.current().with(span))
                .setAttribute("operation", "child-operation")
                .startSpan();
            
            try {
                logger.info("Inside child span context");
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                childSpan.end();
            }
            
            return "Hello from OpenTelemetry Tracing! Custom spans created.";
        } finally {
            span.end();
        }
    }

    /**
     * Demonstrates combined observability with metrics, traces, and logs.
     */
    @GetMapping("/combined")
    @Observed(name = "arconia.combined.operation", lowCardinalityKeyValues = {"type=combined"})
    public String combinedObservability() {
        logger.info("Combined observability endpoint called");
        
        meter.counterBuilder("arconia.combined.requests")
            .build()
            .add(1L, Attributes.builder()
                .put("source", "combined-endpoint")
                .build());

        Span span = tracer.spanBuilder("arconia.combined.custom.span")
            .setAttribute("source", "combined-endpoint")
            .startSpan();
        
        try {
            logger.info("Executing combined observability logic");
            return "Hello from Combined Observability! Metrics, traces, and logs recorded.";
        } finally {
            span.end();
        }
    }
}
