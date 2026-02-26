# Java Backend / DB Architecture Knowledge Base

When a user asks to architect a Java backend system, web architecture, or database layer, refer to these industry standards to draw the architecture diagram.

## Common Architecture Patterns

### 1. Standard Three-Tier Web Service
- **Access Layer**: Nginx / Spring Cloud Gateway / Load Balancer
- **Service Layer**: Spring Boot Applications (User Service, Order Service, Product Service, etc.)
- **Data Layer**: MySQL (Primary Relational Database) + Redis (In-memory Cache)

### 2. High Concurrency / E-Commerce System
- Add **Message Queue (Kafka / RabbitMQ / RocketMQ)** between Service and Data/Other services for async processing, decoupling, and peak shaving.
- **Database Architecture**: Master-Slave MySQL for Read/Write splitting. Sharding (MyCat / ShardingSphere) for massive data.
- **Search System**: Elasticsearch / Logstash / Kibana (ELK) for complex text searches instead of querying MySQL directly like `LIKE %...%`.

### 3. Distributed Coordination & Tasks
- **Distributed Lock / Cache**: Redis Cluster
- **Job Scheduling**: XXL-JOB / ElasticJob
- **Distributed Transaction**: Seata

## Drawing Guidelines (Mapping to draw.io XML)
- **Hierarchy**: Always draw the Access Layer at the Top, Service Layer in the Middle, and Data/Middleware Layer at the Bottom.
- **Grouping**: Group the layers using `swimlane` (e.g., Access, Service, Data). Give the swimlane a distinctly light background color.
- **Color System**: Use distinct colors (e.g., Blue for Services, Green for DBs/Storage, Orange for message queues/middleware).
- **Connections**: Show the request flow with directed edges (e.g., Gateway -> Service -> Redis -> MySQL). Do NOT criss-cross lines.