# Spring Boot Arconia Example

A comprehensive Spring Boot project using the [Arconia Framework](https://arconia.io/) demonstrating integration with PostgreSQL, Redis, Kafka, Grafana/OpenTelemetry, and Kubernetes.

## 🛠️ Technologies

- **Spring Boot 3.5.8** - Production-ready Spring application framework
- **Arconia Framework 0.19.0** - Modern enterprise framework for Java and Spring Boot
- **PostgreSQL** - Relational database with Spring Data JPA and Flyway migrations
- **Redis** - In-memory cache with Spring Data Redis
- **Apache Kafka** - Event streaming platform with Spring Kafka
- **OpenTelemetry** - Observability framework for metrics, traces, and logs
- **Grafana Stack** - Visualization with Prometheus, Tempo, and Loki
- **Kubernetes** - Container orchestration manifests

## 🏗️ Project Structure

```
spring-boot-arconia/
├── src/
│   ├── main/
│   │   ├── java/io/arconia/demo/
│   │   │   ├── config/         # Configuration classes
│   │   │   ├── controller/     # REST controllers
│   │   │   ├── entity/         # JPA and Redis entities
│   │   │   ├── kafka/          # Kafka producers and consumers
│   │   │   ├── repository/     # Data repositories
│   │   │   ├── service/        # Business services
│   │   │   └── Application.java
│   │   └── resources/
│   │       ├── db/migration/   # Flyway migrations
│   │       └── application.yml
│   └── test/
│       └── java/io/arconia/demo/
├── k8s/                        # Kubernetes manifests
├── build.gradle                # Gradle build configuration
└── README.md
```

## 🚀 Quick Start

### Prerequisites

- Java 21+
- Docker/Podman (for dev services)
- Gradle 8.14+

### Running Locally

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-org/spring-boot-arconia.git
   cd spring-boot-arconia
   ```

2. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```
   
   Arconia Dev Services will automatically provision PostgreSQL, Redis, Kafka, and LGTM (Loki, Grafana, Tempo, Mimir) containers.

3. **Access the application:**
   - Application: http://localhost:8080
   - Actuator: http://localhost:8080/actuator
   - Health: http://localhost:8080/actuator/health

### Building

```bash
# Build the project
./gradlew build

# Build Docker image
./gradlew bootBuildImage
```

## 📚 API Examples

### Books API (PostgreSQL)

```bash
# Get all books
curl http://localhost:8080/api/books

# Get book by ID
curl http://localhost:8080/api/books/1

# Create a new book
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"New Book","author":"Author Name","isbn":"978-1234567890","publishedYear":2024}'

# Search books by title
curl "http://localhost:8080/api/books/search?title=Spring"

# Update a book
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated Title","author":"Updated Author","isbn":"978-1234567890","publishedYear":2024}'

# Delete a book
curl -X DELETE http://localhost:8080/api/books/1
```

### Cache API (Redis)

```bash
# Create a session
curl -X POST http://localhost:8080/api/cache/sessions \
  -H "Content-Type: application/json" \
  -d '{"userId":"user123","username":"john_doe","data":"session-data"}'

# Get all sessions
curl http://localhost:8080/api/cache/sessions

# Cache a value
curl -X POST http://localhost:8080/api/cache/values \
  -H "Content-Type: application/json" \
  -d '{"key":"my-key","value":"my-value","ttl":3600}'

# Get cached value
curl http://localhost:8080/api/cache/values/my-key
```

### Observability API (OpenTelemetry)

```bash
# Micrometer programmatic observation
curl http://localhost:8080/api/observability/micrometer/programmatic

# Micrometer declarative observation
curl http://localhost:8080/api/observability/micrometer/declarative

# OpenTelemetry metrics
curl http://localhost:8080/api/observability/otel/metrics

# OpenTelemetry traces
curl http://localhost:8080/api/observability/otel/traces

# Combined observability
curl http://localhost:8080/api/observability/combined
```

## ⚙️ Configuration

### application.yml

Key configuration options:

```yaml
spring:
  application:
    name: spring-boot-arconia
  
  # PostgreSQL
  datasource:
    url: jdbc:postgresql://localhost:5432/arconia_db
    username: arconia
    password: arconia
  
  # Redis
  data:
    redis:
      host: localhost
      port: 6379
  
  # Kafka
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: arconia-consumer-group

# OpenTelemetry
arconia:
  otel:
    metrics:
      exporter:
        interval: 5s
    resource:
      contributors:
        host:
          enabled: true
        java:
          enabled: true
```

## ☸️ Kubernetes Deployment

### Deploy to Kubernetes

```bash
# Create namespace and deploy infrastructure
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/postgresql.yaml
kubectl apply -f k8s/redis.yaml
kubectl apply -f k8s/kafka.yaml

# Deploy observability stack
kubectl apply -f k8s/otel-collector.yaml
kubectl apply -f k8s/observability.yaml

# Deploy application
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/deployment.yaml
```

### Access Services

```bash
# Port forward to application
kubectl port-forward -n arconia svc/spring-boot-arconia 8080:8080

# Port forward to Grafana
kubectl port-forward -n arconia svc/grafana 3000:3000
```

### Kubernetes Manifests

| File | Description |
|------|-------------|
| `k8s/namespace.yaml` | Namespace for the application |
| `k8s/configmap.yaml` | Application configuration and secrets |
| `k8s/deployment.yaml` | Application deployment and service |
| `k8s/postgresql.yaml` | PostgreSQL StatefulSet |
| `k8s/redis.yaml` | Redis StatefulSet |
| `k8s/kafka.yaml` | Kafka StatefulSet (KRaft mode) |
| `k8s/otel-collector.yaml` | OpenTelemetry Collector |
| `k8s/observability.yaml` | Grafana, Prometheus, Tempo, Loki |

## 📊 Observability

### Grafana Dashboards

Access Grafana at http://localhost:3000 (admin/admin)

- **Prometheus** - Metrics from Spring Boot Actuator and OpenTelemetry
- **Tempo** - Distributed traces
- **Loki** - Application logs

### Key Metrics

- `http.server.requests` - HTTP request metrics
- `jvm.memory.used` - JVM memory usage
- `arconia.api.requests` - Custom application metrics
- `micrometer.greeting` - Micrometer observation metrics

## 🔧 Development

### Running Tests

```bash
./gradlew test
```

### Dev Services

Arconia automatically provisions the following containers during development:

- PostgreSQL 16
- Redis 7
- Kafka 3.7 (KRaft mode)
- LGTM Stack (Loki, Grafana, Tempo, Mimir)

### Hot Reload

Spring Boot DevTools is enabled for automatic application restarts during development.

## 📖 References

- [Arconia Framework Documentation](https://arconia.io/)
- [Arconia Examples Repository](https://github.com/arconia-io/arconia-examples)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Data Redis Documentation](https://docs.spring.io/spring-data/redis/docs/current/reference/html/)
- [Spring Kafka Documentation](https://docs.spring.io/spring-kafka/docs/current/reference/html/)
- [OpenTelemetry Java Documentation](https://opentelemetry.io/docs/instrumentation/java/)

## 📄 License

This project is licensed under the Apache License 2.0.
