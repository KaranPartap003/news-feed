# RAG-Based News Feed

A scalable, real-time news aggregation platform combining Retrieval‑Augmented Generation (RAG), Apache Kafka streaming, Redis vector search, PostgreSQL pagination, and a React infinite‑scroll frontend to deliver personalized article recommendations.

---

## Overview

This project ingests live news from multiple APIs, streams them through Kafka, persists general articles in PostgreSQL, and stores semantic embeddings in Redis for similarity‑based recommendations. A React UI presents a unified, infinite‑scroll feed that interleaves paginated news with personalized suggestions based on each user’s last interactions.

---

## Features

- **Real‑Time Streaming**  
  Fetch live articles from NewsData.io and The Guardian APIs and publish into Kafka topics for asynchronous, high‑throughput ingestion.

- **Vector Search & Recommendations**  
  Embed articles via Ollama (or custom model), store embeddings in Redis Vector Store, and perform k‑NN semantic searches for personalized recommendations.

- **Efficient Pagination**  
  Store “general news” in PostgreSQL with structured columns, and expose paginated REST endpoints via Spring Data JPA.

- **User Activity Tracking**  
  Record user interactions (clicks) to dynamically adjust recommendations on each login.

- **Unified API Gateway**  
  Spring Boot merges paginated and recommended lists, tagging articles with an `isRecommended` flag for frontend styling.

- **Infinite Scroll Frontend**  
  React.js with infinite scroll implementation which dynamically loads mixed batches of general and recommended articles.

- **Secure Authentication** 
  Spring Security + JWT for stateless login and role-based access control.  (still in progress)

---

## Tech Stack

- **Backend:** Java 17, Spring Boot, Spring Security, Apache Kafka  
- **Vector Search:** Redis Vector Store, Ollama EmbeddingModel  
- **Database:** PostgreSQL (JDBC), Hibernate (JPA)  
- **Frontend:** React.js, Node.js  
- **Authentication:** JWT  
- **Dev Tools:** Maven, npm, Docker (optional), Postman

---

## Getting Started

### Prerequisites

- Java 17+ & Maven  
- Node.js 16+ & npm  
- Apache Kafka (broker + Zookeeper)  
- Redis with Vector/RediSearch module  
- PostgreSQL 12+

### Configuration

Create `src/main/resources/application.properties` and add:

```properties
# Kafka
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=news-group

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/newsdb
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.jpa.properties.hibernate.jdbc.batch_size=10

# Redis Vector Store
redis.uri=YOUR_REDIS_URI
vector.index-name=YOUR_CUSTOM_INDEX_NAME

# NewsData.io API
newsdata.api.key=YOUR_NEWSDATA_KEY

# Guardian API
guardian.api.key=YOUR_GUARDIAN_KEY
`````

## Kafka Topic Setup
```properties
@Bean
public NewTopic redisTopic() {
    return TopicBuilder.name("redis-data").build();
}

@Bean
public NewTopic sqlTopic() {
    return TopicBuilder.name("sql-data").build();
}
`````
