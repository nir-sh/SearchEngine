# Search Engine Project

## Overview
This project implements a configurable web crawler that behaves like a miniature Google:
- **Crawl any starting URL**: The crawler traverses pages from a given starting URL.
- **Configurable depth and duration**: Control how deep the crawler goes and how long it runs.
- **Content indexing**: Pages are indexed in **Elasticsearch**, enabling powerful and fast searches.
- **Message Queue Integration**: Built using **Kafka** for message queuing and **Redis** for progress tracking.

## Features
1. **Crawling**:
   - Traverses pages via hyperlinks and avoids revisiting links.
   - Supports configurable crawling depth and max URLs.

2. **Elasticsearch Integration**:
   - Stores crawled data for fast search and retrieval.
   - Allows querying indexed web content efficiently.

3. **Kafka**:
   - Distributes crawl tasks using a message queue for scalability.

4. **Redis**:
   - Tracks crawling progress and stores status updates.

5. **Swagger Integration**:
   - Offers an interactive API interface for testing endpoints.

---

## Installation and Setup

### Prerequisites
- **Java 11 or later**
- **Maven** (for building the project)
- **Docker** (for running Kafka, Redis, and Elasticsearch)
- **ElasticSearch instance** (you can use a local or cloud setup)

### Steps
1. **Clone the repository**:
   ```bash
   git clone <your-new-repository-url>
   cd searchengine
   ```

2. **Update Configuration**:
   - Copy the `application-template.properties` to `application.properties`:
     ```bash
     cp src/main/resources/application-template.properties src/main/resources/application.properties
     ```
   - Edit the `application.properties` file to set up your environment.

3. **Build the project**:
   ```bash
   mvn clean install
   ```

4. **Run Docker Containers**:
   Ensure Kafka, Redis, and Elasticsearch are running. Use the following `docker-compose.yml` configuration:
   ```yaml
   version: '3.8'
   services:
     zookeeper:
       image: confluentinc/cp-zookeeper:latest
       ports:
         - 2181:2181
       environment:
         ZOOKEEPER_CLIENT_PORT: 2181
         ZOOKEEPER_TICK_TIME: 2000
     kafka:
       image: confluentinc/cp-kafka:latest
       depends_on:
         - zookeeper
       ports:
         - 9092:9092
       environment:
         KAFKA_BROKER_ID: 1
         KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
         KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
         KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
     redis:
       image: redis
       ports:
         - 6379:6379
     elasticsearch:
       image: docker.elastic.co/elasticsearch/elasticsearch:7.9.2
       environment:
         - discovery.type=single-node
       ports:
         - 9200:9200
   ```

5. **Run the Application**:
   ```bash
   java -jar target/searchengine-0.0.1-SNAPSHOT.jar
   ```

---

## API Endpoints
- **Crawl a URL**:
  - **POST** `/api/crawl`
  - Body:
    ```json
    {
      "url": "https://example.com",
      "depth": 3,
      "maxUrls": 100
    }
    ```

- **Check Crawl Status**:
  - **GET** `/api/crawl/{crawlId}`

- **Send Task to Kafka**:
  - **POST** `/api/sendKafka`
  - Body: Same as `/api/crawl`.

### Swagger Documentation
Access the Swagger UI at: `http://localhost:8080/swagger-ui.html`

---

## Future Enhancements
- Add monitoring for crawl progress via a dashboard.
- Expand supported file types for content extraction (e.g., PDFs).

---

## Contributing
Feel free to open pull requests or report issues. Contributions are welcome!


