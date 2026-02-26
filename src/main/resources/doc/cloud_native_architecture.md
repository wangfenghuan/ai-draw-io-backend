# Cloud Native & Microservices Architecture Knowledge

When a user asks to draw a microservice, cloud-native, or AWS/K8s architecture diagram, structure the components as follows:

## Core Components & Layers

### 1. Client & Edge Layer
- Web App / Mobile App / IoT Devices
- **Edge Routing**: CDN (Cloudflare / AWS CloudFront) -> WAF (Web Application Firewall)

### 2. Traffic Management & API Layer
- **Ingress**: Kubernetes Ingress / AWS ALB / Nginx
- **API Gateway**: Spring Cloud Gateway / Kong / APISIX
- **Service Mesh**: Istio / Envoy / Linkerd (Handles mTLS, circuit breaking, traffic mirroring)

### 3. Microservices (Compute Layer)
- Business Domains (e.g., Auth Service, Catalog, Cart, Order, Payment)
- Deployed typically via K8s Pods / AWS ECS / EKS / Docker Containers.

### 4. Service Governance (Spring Cloud / K8s native)
- **Service Discovery & Config**: Nacos / Eureka / Consul
- **Circuit Breaker**: Sentinel / Resilience4j
- **Tracing**: SkyWalking / Jaeger / Zipkin

### 5. Data & Storage Layer
- Relational DBs (AWS RDS, Aurora, MySQL, PostgreSQL)
- NoSQL (MongoDB, DynamoDB)
- Object Storage (Amazon S3, MinIO)

### 6. Observability
- **Metrics**: Prometheus & Grafana
- **Logging**: ELK Stack (Elasticsearch, Logstash, Kibana) / Fluentd

## Drawing Guidelines for AI
- **Containerization**: Use grouped boxes (`swimlane` with dashed borders) to represent VPCs, Kubernetes Clusters, or Namespaces.
- **Flow**: Position the Edge/Gateway at the top. Route traffic downwards to specific microservices.
- **Microservice Best Practice**: Ensure each microservice connects to its OWN dedicated database schema in the diagram (Avoid drawing a "God" DB that all services connect to, unless specified as legacy).