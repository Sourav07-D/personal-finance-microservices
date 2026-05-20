# 📌 Project Overview

**Personal Finance Intelligence Platform** is a production-style microservices-based backend system designed to help users manage and analyze their financial transactions efficiently and securely.

The platform provides:

* Secure JWT-based authentication & authorization
* Transaction management
* Category management
* Advanced analytics
* Redis caching
* API Gateway routing
* Dynamic filtering & pagination
* Role-based security
* Microservice communication using Feign Client

The project was initially designed as a monolith and later refactored into a scalable microservices architecture following real-world backend engineering principles.

---

# 🔥 Key Highlights

✅ Monolith → Microservices Migration
✅ API Gateway with Centralized Routing
✅ JWT Authentication & Role-Based Authorization
✅ Redis Caching for Performance Optimization
✅ Dynamic Filtering + Pagination + Sorting
✅ Feign Client Inter-Service Communication
✅ Centralized Exception Handling
✅ Production-Level Logging & Validation
✅ Query Optimization & Database Indexing
✅ Clean Architecture & Separation of Concerns
✅ Enterprise-Level Backend Design

---

# 🏗️ Architecture / System Design

```text
                        ┌───────────────────┐
                        │      Client       │
                        └─────────┬─────────┘
                                  │
                                  ▼
                     ┌────────────────────────┐
                     │     API Gateway        │
                     │      Port: 8080        │
                     └─────────┬──────────────┘
                               │
 ┌─────────────────────────────┼─────────────────────────────┐
 │                             │                             │
 ▼                             ▼                             ▼
┌───────────────┐    ┌─────────────────┐        ┌─────────────────┐
│ Identity      │    │ Transaction     │        │ Category        │
│ Service       │    │ Service         │        │ Service         │
│ Port: 8081    │    │ Port: 8082      │        │ Port: 8083      │
└──────┬────────┘    └────────┬────────┘        └────────┬────────┘
       │                      │                           │
       └──────────────┬───────┘                           │
                      ▼                                   ▼
              ┌─────────────────┐               ┌─────────────────┐
              │ Analytics       │               │ Redis Cache     │
              │ Service         │               │                 │
              │ Port: 8084      │               └─────────────────┘
              └─────────────────┘
```

---

# ⚙️ Tech Stack

| Category                    | Technologies                |
| --------------------------- | --------------------------- |
| Language                    | Java 21                     |
| Backend Framework           | Spring Boot 3               |
| Architecture                | Microservices               |
| Security                    | Spring Security + JWT       |
| API Gateway                 | Spring Cloud Gateway        |
| Database                    | PostgreSQL                  |
| ORM                         | Spring Data JPA + Hibernate |
| Cache                       | Redis                       |
| Inter-Service Communication | OpenFeign                   |
| Build Tool                  | Maven                       |
| Validation                  | Jakarta Validation          |
| Logging                     | SLF4J + Logback             |
| Containerization            | Docker                      |
| API Testing                 | Postman                     |
| Version Control             | Git & GitHub                |

---

# 🧠 Why These Technologies Were Used

### Spring Boot

Chosen for rapid enterprise-grade backend development with strong ecosystem support.

### Microservices

Improves scalability, maintainability, and independent deployment of services.

### Redis

Used to reduce database load and improve analytics performance through caching.

### PostgreSQL

Reliable relational database with excellent transactional consistency.

### Spring Cloud Gateway

Acts as a centralized API entry point and routing layer.

### JWT Authentication

Provides stateless and scalable authentication for distributed systems.

### Feign Client

Simplifies inter-service REST communication.

---

# 📂 Project Structure

```text
personal-finance-microservices/
│
├── identity-service/
│
├── transaction-service/
│
├── category-service/
│
├── analytics-service/
│
├── gateway-service/
│
├── docker-compose.yml
│
└── README.md
```

---

# 🔄 System Workflow

## User Authentication Flow

```text
Client
   ↓
API Gateway
   ↓
Identity Service
   ↓
JWT Token Generation
   ↓
Client Stores Token
```

---

## Transaction Request Flow

```text
Client
   ↓
API Gateway
   ↓
Transaction Service
   ↓
Database
   ↓
Response
```

---

## Analytics Flow with Redis

```text
Client
   ↓
API Gateway
   ↓
Analytics Service
   ↓
Redis Cache Check
   ↓
(Cache Hit → Return Data)
(Cache Miss → Query DB → Store in Redis → Return Data)
```

---

# ✨ Features

## 🔐 Authentication & Security

* JWT Access & Refresh Tokens
* Stateless Authentication
* Role-Based Authorization
* Gateway-Level JWT Validation
* Secure Password Encryption using BCrypt

---

## 💳 Transaction Management

* Create Transactions
* Update Transactions
* Delete Transactions
* Pagination & Sorting
* Dynamic Filtering

---

## 📊 Analytics

* Total Expense Calculation
* Category-Wise Expense Summary
* Top Spending Category
* Date Range Analytics

---

## 🚀 Performance Optimization

* Redis Caching
* Query Optimization
* Lazy Loading
* Pagination
* Database Indexing

---

# 🧱 Design Decisions

## Why Analytics Was Separated

Initially analytics logic existed inside `TransactionService`, which violated:

* Single Responsibility Principle
* Separation of Concerns

By introducing `AnalyticsService`:

✅ Better maintainability
✅ Independent scalability
✅ Cleaner architecture
✅ Reduced service coupling

---

## Why API Gateway Was Introduced

Without API Gateway:

```text
Frontend → Multiple Services Directly
```

After introducing gateway:

```text
Frontend → Gateway → Services
```

Benefits:

✅ Centralized routing
✅ Better security
✅ Easier frontend integration
✅ Scalable architecture

---

# 🔐 Authentication & Security

## JWT-Based Authentication

The platform uses:

* Access Token
* Refresh Token
* Stateless Authentication

### Authentication Flow

```text
Login → JWT Issued → Client Sends Token → Services Validate JWT
```

---

## Role-Based Authorization

Roles Supported:

* USER
* ADMIN

Example:

```java
@PreAuthorize("hasRole('ADMIN')")
```

---

# 🗄️ Database Design

## Core Entities

### User

* id
* name
* email
* password
* role

### Transaction

* id
* amount
* description
* date
* userId
* categoryId

### Category

* id
* name
* userId

---

# 📌 Database Optimizations

## Indexing

Indexes added on:

* user_id
* category_id
* user_id + date

### Why?

To optimize:

* Filtering
* Aggregation
* Date-range queries

---

# 🔁 Microservices Communication

The project uses:

## OpenFeign Client

Example:

```java
@FeignClient(name = "category-service")
```

Used for:

* Service-to-service communication
* Decoupled REST calls
* Cleaner codebase

---

# ⚡ Caching / Performance Optimization

## Redis Caching

Implemented using:

```java
@Cacheable
@CacheEvict
```

### Cached Operations

* Total Expense
* Category Summary

---

## Why Redis?

Without caching:

```text
Every analytics request hits database
```

With Redis:

```text
Frequently accessed data served from memory
```

Benefits:

✅ Reduced DB load
✅ Faster API response
✅ Better scalability

---

# 🐳 Docker Setup

## Run Redis

```bash
docker run --name redis-container -p 6379:6379 redis
```

---

## Run PostgreSQL

```bash
docker run --name postgres-container \
-e POSTGRES_PASSWORD=password \
-p 5432:5432 postgres
```

---

## Docker Compose (Recommended)

```bash
docker-compose up -d
```

---

# 🧪 Testing

## Tools Used

* Postman
* IntelliJ HTTP Client

---

## Test Cases Covered

✅ Authentication
✅ JWT Validation
✅ Role Authorization
✅ CRUD Operations
✅ Redis Cache Validation
✅ Pagination & Filtering
✅ Gateway Routing
✅ Error Handling

---

# 📦 Installation & Setup

# Clone Repository

```bash
git clone https://github.com/yourusername/personal-finance-intelligence-platform.git
```

---

# Navigate to Project

```bash
cd personal-finance-intelligence-platform
```

---

# Build Project

```bash
mvn clean install
```

---

# Run Services

Run services in this order:

```text
1. identity-service
2. transaction-service
3. category-service
4. analytics-service
5. gateway-service
```

---

# 🌍 Environment Variables

```properties
JWT_SECRET=your-secret-key

DB_URL=jdbc:postgresql://localhost:5432/db_name

DB_USERNAME=postgres

DB_PASSWORD=password

REDIS_HOST=localhost

REDIS_PORT=6379
```

---

# 📡 API Endpoints

# Authentication

## Login

```http
POST /api/auth/login
```

### Request

```json
{
  "email": "user@gmail.com",
  "password": "password"
}
```

### Response

```json
{
  "accessToken": "jwt-token",
  "refreshToken": "refresh-token"
}
```

---

# Transactions

## Create Transaction

```http
POST /api/transactions/user/{userId}/category/{categoryId}
```

---

# Analytics

## Total Expense

```http
GET /api/analytics/user/{userId}/total-expense
```

---

# 🚨 Error Handling & Logging

## Centralized Exception Handling

Implemented using:

```java
@RestControllerAdvice
```

---

## Structured Error Response

```json
{
  "timestamp": "2026-05-12T12:00:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Transaction not found"
}
```

---

## Logging

Implemented using:

* SLF4J
* Logback

Benefits:

✅ Easier debugging
✅ Better monitoring
✅ Production observability

---

# 🔍 Best Practices Followed

✅ Clean Architecture
✅ SOLID Principles
✅ Stateless Authentication
✅ DTO Pattern
✅ Global Exception Handling
✅ Validation Layer
✅ Separation of Concerns
✅ Caching Strategy
✅ Layered Architecture
✅ Centralized API Routing

---

# 🚀 Production Improvements

Future enterprise-level enhancements:

* Eureka Service Discovery
* Circuit Breaker (Resilience4j)
* Distributed Tracing (Zipkin)
* Centralized Logging (ELK Stack)
* Kafka Event-Driven Architecture
* Kubernetes Deployment
* CI/CD Pipeline
* Prometheus & Grafana Monitoring

---

# ⚠️ Challenges Faced

* Monolith to Microservices migration
* JWT propagation across services
* Redis cache invalidation
* Distributed security handling
* Gateway routing configuration
* Avoiding N+1 query problems
* Designing scalable analytics

---

# 📚 Learning Outcomes

Through this project, I gained hands-on experience in:

* Microservices architecture
* Enterprise backend engineering
* Spring Security & JWT
* API Gateway design
* Redis caching
* Inter-service communication
* Database optimization
* Production-ready system design

---

# 📸 Screenshots / Demo

## Suggested Screenshots

* Authentication Flow
* Gateway Routing
* Redis Cache Hit Logs
* Swagger UI
* Postman API Testing
* Microservices Architecture Diagram

---

# 🌐 Deployment Link

```text
Coming Soon
```

---

# 🤝 Contributing Guidelines

1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push branch
5. Open Pull Request

---

# 📄 License

This project is licensed under the MIT License.

---

# 👨‍💻 Author Information

## Sourav Das

* LinkedIn: [https://www.linkedin.com/in/sourav-das-654234248/](https://www.linkedin.com/in/sourav-das-654234248/)
* GitHub: [https://github.com/your-github-username](https://github.com/your-github-username)

---

# ⭐ Final Note

This project demonstrates strong backend engineering concepts including:

* Scalable microservices architecture
* Enterprise security implementation
* Performance optimization
* Clean code practices
* Distributed system fundamentals

It is designed to reflect real-world backend development standards followed in modern software engineering teams.
