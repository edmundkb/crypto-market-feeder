# crypto-market-feeder

A high-throughput, reactive data ingest microservice designed to stream real-time cryptocurrency ticks from the Coinbase WebSocket API and publish them directly to a RabbitMQ Topic Exchange.

---

## Architecture Overview

This service acts as the decoupled ingestion layer for a larger cryptocurrency alerting ecosystem. Its only responsibility is to maintain a stable, low-latency WebSocket connection with external market providers, normalize incoming payloads, and push them downstream. It does not handle alerting logic or state persistence.

---

## Technical Stack

* Java 21
* Spring Boot 3.5.x (WebFlux / WebSocket Client)
* Spring AMQP (RabbitMQ Integration)

---

## Getting Started

### 1. Prerequisites
Ensure you have Docker and Maven installed on your machine.

### 2. Start the Message Broker
Run a local RabbitMQ instance containerized via Docker with the management console enabled:

```bash
docker run -d --name crypto-rabbit \
  -p 5672:5672 -p 15672:15672 \
  rabbitmq:3-management
```

### 3. Build and Run the Application
Navigate to the root directory, compile the project dependencies, and execute the boot task:

```bash
mvn clean install
mvn spring-boot:run
```

---

## Messaging Specifications

* Exchange Name: market.data
* Exchange Type: Topic
* Routing Keys: market.data.btc, market.data.eth
* Content Type: application/json

Downstream consumers (such as the alert engine) should bind their own private queues to the `market.data` exchange using wildcards (e.g., `market.data.#`) to process the stream asynchronously.

---

## Configuration

Environment variables can be configured within `src/main/resources/application.properties`.

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```
