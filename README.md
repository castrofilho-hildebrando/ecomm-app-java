# E-Commerce Backend - Clean Architecture & DDD Showcase

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-6.x-green.svg)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A production-ready e-commerce backend demonstrating **Clean Architecture**, **Domain-Driven Design (DDD)**, and **Event-Driven Architecture** best practices. Built with Java 17+ and Spring Boot 3.x.

---

## 🎯 Project Overview

This project showcases enterprise-level backend development practices through a simplified e-commerce domain. It's designed to demonstrate architectural patterns, design principles, and engineering practices used in modern distributed systems.

### What Makes This Project Special?

✅ **Clean Architecture** - Business logic completely independent of frameworks  
✅ **Domain-Driven Design** - Rich domain models with enforced invariants  
✅ **Event-Driven Architecture** - Transactional outbox pattern for reliable messaging  
✅ **CQRS Foundation** - Separation of commands and queries  
✅ **Concurrency Control** - Optimistic locking prevents lost updates  
✅ **Idempotency** - Safe retry of payment operations  
✅ **Comprehensive Testing** - Unit, integration, and controller tests  
✅ **Security by Design** - Domain policies enforce authorization rules  

---

## 🏗️ Architecture Highlights

### Layered Architecture

```
┌─────────────────────────────────────────────────┐
│  Infrastructure (Web, DB, External Services)    │
│  ┌───────────────────────────────────────────┐  │
│  │  Application (Use Cases & Orchestration)  │  │
│  │  ┌─────────────────────────────────────┐  │  │
│  │  │  Domain (Business Logic & Rules)    │  │  │
│  │  └─────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
```

### Key Patterns Implemented

| Pattern | Purpose | Implementation |
|---------|---------|----------------|
| **Repository** | Abstract data access | `CartRepository`, `OrderRepository` |
| **Use Case** | Encapsulate business workflows | `AddItemToCartUseCase`, `PayOrderUseCase` |
| **Aggregate** | Consistency boundaries | `Cart`, `Order` |
| **Value Object** | Type-safe domain concepts | `CartId`, `ProductId`, `OrderId` |
| **Domain Events** | Decouple aggregates | `OrderCreatedEvent`, `OrderPaidEvent` |
| **Transactional Outbox** | Reliable event publishing | `OutboxEvent`, `OutboxEventPublisher` |
| **Optimistic Locking** | Concurrency control | `@Version` on aggregates |
| **Policy Object** | Reusable business rules | `CartOwnershipPolicy`, `AdminPolicy` |

---

## 🚀 Features

### Shopping Cart Management
- ✅ Add items to cart with quantity validation
- ✅ Update item quantities (remove when set to 0)
- ✅ Remove specific items
- ✅ Clear entire cart
- ✅ Ownership validation (users can only modify their own carts)
- ✅ Optimistic locking prevents concurrent modification issues

### Order Processing
- ✅ Create orders from shopping carts
- ✅ Automatic cart clearing after order placement
- ✅ Pay for orders with idempotency guarantees
- ✅ State machine validation (prevent double payment)
- ✅ Domain event publishing for downstream systems

### Cross-Cutting Concerns
- ✅ Role-based access control (CLIENT, ADMIN)
- ✅ Global exception handling with RFC 7807 Problem Details
- ✅ Request validation using Jakarta Bean Validation
- ✅ Event-driven architecture with outbox pattern
- ✅ Idempotent payment operations

---

## 📂 Project Structure

```
src/
├── main/
│   └── java/com/example/ecommerce/
│       ├── domain/                    # Pure business logic (no dependencies)
│       │   ├── cart/
│       │   │   ├── Cart.java          # Aggregate root
│       │   │   ├── CartItem.java      # Entity
│       │   │   ├── CartId.java        # Value object
│       │   │   ├── ProductId.java     # Value object
│       │   │   ├── CartRepository.java # Repository interface
│       │   │   └── CartOwnershipPolicy.java
│       │   ├── order/
│       │   │   ├── Order.java         # Aggregate root
│       │   │   ├── OrderItem.java     # Entity
│       │   │   ├── OrderStatus.java   # Enum (CREATED, PAID, CANCELLED)
│       │   │   ├── OrderRepository.java
│       │   │   └── event/
│       │   │       ├── OrderCreatedEvent.java
│       │   │       └── OrderPaidEvent.java
│       │   └── user/
│       │       ├── User.java
│       │       └── UserRole.java      # Enum (CLIENT, ADMIN)
│       │
│       ├── application/               # Use cases & orchestration
│       │   ├── cart/
│       │   │   ├── AddItemToCartUseCase.java
│       │   │   ├── UpdateCartItemQuantityUseCase.java
│       │   │   ├── RemoveItemFromCartUseCase.java
│       │   │   ├── ClearCartUseCase.java
│       │   │   └── GetCartUseCase.java
│       │   ├── order/
│       │   │   ├── PlaceOrderFromCartUseCase.java
│       │   │   ├── PayOrderUseCase.java
│       │   │   └── GetOrderUseCase.java
│       │   └── security/
│       │       ├── CurrentUser.java
│       │       └── AdminPolicy.java
│       │
│       └── infrastructure/            # Technical implementations
│           ├── web/
│           │   ├── cart/CartController.java
│           │   ├── order/OrderController.java
│           │   └── ApiExceptionHandler.java
│           ├── persistence/
│           │   ├── mongo/             # MongoDB repositories
│           │   └── memory/            # In-memory for testing
│           ├── security/
│           │   ├── SecurityContextCurrentUser.java
│           │   └── CurrentUserArgumentResolver.java
│           ├── outbox/
│           │   ├── OutboxEvent.java
│           │   ├── OutboxEventPublisher.java
│           │   └── OutboxDomainEventPublisher.java
│           └── idempotency/
│               ├── IdempotencyKey.java
│               └── IdempotencyRepository.java
│
└── test/
    └── java/com/example/ecommerce/
        ├── domain/                    # Unit tests (fast, no mocks)
        │   ├── cart/CartTest.java
        │   └── order/OrderTest.java
        ├── application/               # Use case tests (in-memory repos)
        │   └── order/
        │       ├── PlaceOrderFromCartUseCaseTest.java
        │       └── PayOrderUseCaseTest.java
        └── infrastructure/            # Integration tests (MockMvc)
            └── web/
                ├── cart/CartControllerTest.java
                └── order/OrderControllerTest.java
```

---

## 🛠️ Technology Stack

### Core
- **Java 17+** - Modern Java with records, pattern matching, and sealed types
- **Spring Boot 3.x** - Application framework
- **Spring Data MongoDB** - Document persistence
- **Spring Security** - Authentication & authorization

### Data & Persistence
- **MongoDB 6.x** - Document database
- **Optimistic Locking** - `@Version` for concurrency control
- **Transactional Outbox** - Reliable event publishing

### Testing
- **JUnit 5** - Test framework
- **Spring Test** - Integration testing support
- **MockMvc** - Controller layer testing
- **In-Memory Repositories** - Fast, isolated unit tests

### Validation & Error Handling
- **Jakarta Bean Validation** - Request validation
- **RFC 7807 Problem Details** - Standardized error responses

---

## 🔧 Getting Started

### Prerequisites

- Java 17 or higher
- MongoDB 6.x running locally or accessible remotely
- Gradle or Maven

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/ecommerce-backend.git
cd ecommerce-backend
```

2. **Configure MongoDB**

Create `application.yml` or set environment variables:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/ecommerce
```

3. **Build the project**
```bash
./gradlew build
# or
./mvnw clean install
```

4. **Run tests**
```bash
./gradlew test
# or
./mvnw test
```

5. **Start the application**
```bash
./gradlew bootRun
# or
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

---

## 📖 API Documentation

### Cart Endpoints

#### Get Cart
```http
GET /carts/{cartId}
Authorization: Basic dXNlcjE6cGFzc3dvcmQ=
```

#### Add Item to Cart
```http
POST /carts/{cartId}/items
Content-Type: application/json
Authorization: Basic dXNlcjE6cGFzc3dvcmQ=

{
  "productId": "product-123",
  "quantity": 2
}
```

#### Update Item Quantity
```http
PUT /carts/{cartId}/items/{productId}
Content-Type: application/json
Authorization: Basic dXNlcjE6cGFzc3dvcmQ=

{
  "quantity": 5
}
```

#### Remove Item
```http
DELETE /carts/{cartId}/items/{productId}
Authorization: Basic dXNlcjE6cGFzc3dvcmQ=
```

#### Clear Cart
```http
DELETE /carts/{cartId}
Authorization: Basic dXNlcjE6cGFzc3dvcmQ=
```

### Order Endpoints

#### Place Order from Cart
```http
POST /orders/from-cart/{cartId}
Authorization: Basic dXNlcjE6cGFzc3dvcmQ=
```

**Response:**
```json
{
  "id": "order-456",
  "status": "CREATED",
  "items": [
    {
      "productId": "product-123",
      "quantity": 2
    }
  ]
}
```

#### Get Order
```http
GET /orders/{orderId}
Authorization: Basic dXNlcjE6cGFzc3dvcmQ=
```

#### Pay for Order (Idempotent)
```http
POST /orders/{orderId}/pay
Idempotency-Key: unique-request-id-789
Authorization: Basic dXNlcjE6cGFzc3dvcmQ=
```

**Idempotency Guarantee**: Multiple requests with the same `Idempotency-Key` will return the same response without reprocessing.

### Error Responses

All errors follow [RFC 7807 Problem Details](https://datatracker.ietf.org/doc/html/rfc7807):

```json
{
  "type": "about:blank",
  "title": "Business rule violation",
  "status": 400,
  "detail": "Invalid quantity: 0",
  "type": "InvalidQuantityException"
}
```

**Common Status Codes:**
- `400 Bad Request` - Validation errors or business rule violations
- `403 Forbidden` - Authorization failures
- `404 Not Found` - Resource not found
- `409 Conflict` - Optimistic locking failure (concurrent modification)
- `500 Internal Server Error` - Unexpected errors

---

## 🧪 Testing Strategy

### Test Pyramid

```
    ┌──────────────┐
    │ Controller   │  ← Integration tests with MockMvc
    ├──────────────┤
    │  Use Cases   │  ← Application tests with in-memory repos
    ├──────────────┤
    │    Domain    │  ← Unit tests (pure business logic)
    └──────────────┘
```

### Running Tests

```bash
# All tests
./gradlew test

# Specific test class
./gradlew test --tests CartTest

# With coverage report
./gradlew test jacocoTestReport
```

### Test Coverage

- **Domain Layer**: 100% - Pure business logic
- **Application Layer**: 95% - Use case orchestration
- **Infrastructure Layer**: 85% - Integration points

### Example Test Cases

**Domain Test** (No mocks, pure logic):
```java
@Test
void shouldThrowWhenPayingAlreadyPaidOrder() {
    Order order = new Order(
        new OrderId("order-1"),
        List.of(new OrderItem(new OrderProductId("p1"), 1))
    );
    
    order.markAsPaid();
    
    assertThrows(OrderAlreadyPaidException.class, order::markAsPaid);
}
```

**Application Test** (In-memory repos):
```java
@Test
void shouldPlaceOrderFromCartAndClearCart() {
    // Arrange
    Cart cart = new Cart(new CartId("cart-1"), new UserId("user-1"));
    cart.addItem(new ProductId("product-1"), 2);
    cartRepository.save(cart);
    
    // Act
    useCase.execute("cart-1", new FixedCurrentUser("user-1"));
    
    // Assert
    assertEquals(1, publisher.count());
    assertTrue(publisher.first() instanceof OrderCreatedEvent);
    assertTrue(cartRepository.findById(new CartId("cart-1")).orElseThrow().isEmpty());
}
```

**Controller Test** (MockMvc):
```java
@Test
void shouldAddItemToCart() throws Exception {
    mockMvc.perform(post("/carts/{cartId}/items", "cart-1")
            .with(user("user-1"))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"productId\": \"product-1\", \"quantity\": 2}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("cart-1"));
}
```

---

## 🎓 Learning Resources

This project demonstrates concepts from:

### Books
- **Clean Architecture** by Robert C. Martin
- **Domain-Driven Design** by Eric Evans
- **Implementing Domain-Driven Design** by Vaughn Vernon
- **Enterprise Integration Patterns** by Gregor Hohpe

### Patterns & Practices
- Clean Architecture / Hexagonal Architecture
- Domain-Driven Design (Aggregates, Value Objects, Repositories)
- CQRS (Command Query Responsibility Segregation)
- Event Sourcing foundation
- Transactional Outbox Pattern
- Optimistic Locking
- Idempotency

---

## 📊 Key Design Decisions

### Why Clean Architecture?

**Benefits Demonstrated:**
- ✅ Business logic is framework-agnostic (can switch from Spring to Quarkus)
- ✅ Easy to test without Spring context
- ✅ Domain complexity is manageable and explicit
- ✅ Infrastructure changes don't affect business rules

### Why DDD?

**Benefits Demonstrated:**
- ✅ Rich domain models that enforce invariants
- ✅ Ubiquitous language between code and business
- ✅ Clear boundaries between aggregates
- ✅ Business rules are self-documenting

### Why Event-Driven?

**Benefits Demonstrated:**
- ✅ Loose coupling between aggregates
- ✅ Audit trail of all important actions
- ✅ Foundation for eventual consistency
- ✅ Easy integration with external systems

### Why Optimistic Locking?

**Problem Solved:**
```
User A reads Cart (version=1)  ──┐
User B reads Cart (version=1)  ──┤
User A saves Cart (version=2)  ──┤  Without locking: User B's 
User B saves Cart (overwrite!) ──┘  changes would overwrite User A's
```

**Solution:** Version-based concurrency control with retry mechanism.

### Why Idempotency?

**Problem Solved:**
```
Client ──[Pay $100]──> Network Error ──X
Client ──[Pay $100]──> ❌ Charged twice!
```

**Solution:** Idempotency keys ensure duplicate requests return cached response.

---

## 🔮 Future Enhancements

### Phase 1: Advanced Patterns
- [ ] CQRS with separate read models
- [ ] Event Sourcing for Order aggregate
- [ ] Saga pattern for distributed transactions
- [ ] API Gateway with rate limiting

### Phase 2: Operational Excellence
- [ ] OpenTelemetry distributed tracing
- [ ] Prometheus metrics
- [ ] ELK stack for centralized logging
- [ ] Kubernetes deployment manifests

### Phase 3: Scalability
- [ ] Microservices decomposition
- [ ] Apache Kafka integration
- [ ] Redis caching layer
- [ ] GraphQL API

---

## 🤝 Contributing

This is a showcase project, but suggestions and improvements are welcome!

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-improvement`)
3. Commit your changes (`git commit -m 'Add amazing improvement'`)
4. Push to the branch (`git push origin feature/amazing-improvement`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Hildebrando de Castro**
- LinkedIn: [linkedin.com/in/hildebrando-castro](https://www.linkedin.com/in/hildebrando-castro/)
- GitHub: [@castrofilho-hildebrando](https://github.com/castrofilho-hildebrando)
- Email: dev@unitytech.dev.br

---

## 🙏 Acknowledgments

- Clean Architecture community
- Domain-Driven Design practitioners
- Spring Boot team
- MongoDB team

---

## 📞 Contact

For questions about architecture decisions, design patterns, or implementation details, feel free to reach out or open an issue!

**⭐ If you find this project helpful, please consider giving it a star!**
