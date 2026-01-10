# Architecture Documentation

## Table of Contents
- [Overview](#overview)
- [Architectural Patterns](#architectural-patterns)
- [Domain-Driven Design](#domain-driven-design)
- [Layer Organization](#layer-organization)
- [Key Design Patterns](#key-design-patterns)
- [Data Consistency & Concurrency](#data-consistency--concurrency)
- [Event-Driven Architecture](#event-driven-architecture)
- [Security & Authorization](#security--authorization)
- [Testing Strategy](#testing-strategy)

---

## Overview

This e-commerce system is built following **Clean Architecture** and **Domain-Driven Design (DDD)** principles, with a strong emphasis on business logic isolation, testability, and scalability.

### Core Architectural Principles

1. **Domain Independence**: Business rules exist independently of frameworks and infrastructure
2. **Dependency Inversion**: Dependencies point inward toward the domain
3. **Separation of Concerns**: Clear boundaries between layers
4. **Testability**: Core business logic can be tested without external dependencies
5. **Scalability**: Event-driven architecture enables horizontal scaling

---

## Architectural Patterns

### Clean Architecture (Hexagonal Architecture)

The system is organized in concentric layers:

```
┌─────────────────────────────────────────────────┐
│           Infrastructure Layer                  │
│  (Controllers, Repositories, External Services) │
│                                                 │
│  ┌───────────────────────────────────────────┐  │
│  │       Application Layer                   │  │
│  │     (Use Cases, DTOs, Interfaces)         │  │
│  │                                           │  │
│  │  ┌─────────────────────────────────────┐  │  │
│  │  │        Domain Layer                 │  │  │
│  │  │  (Entities, Value Objects, Events)  │  │  │
│  │  └─────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
```

**Dependency Rule**: Source code dependencies must point only inward. Inner layers know nothing about outer layers.

---

## Domain-Driven Design

### Bounded Contexts

The system is organized into three main bounded contexts:

#### 1. Cart Context
- **Aggregate Root**: `Cart`
- **Value Objects**: `CartId`, `ProductId`, `CartItem`
- **Repository**: `CartRepository`
- **Business Rules**:
  - Only cart owner can modify items
  - Quantities must be positive
  - Cart can be cleared when order is placed

#### 2. Order Context
- **Aggregate Root**: `Order`
- **Value Objects**: `OrderId`, `OrderProductId`, `OrderItem`
- **Entities**: `Order` with state transitions
- **Domain Events**: `OrderCreatedEvent`, `OrderPaidEvent`
- **Repository**: `OrderRepository`
- **Business Rules**:
  - Order must have at least one item
  - Only CREATED orders can be paid
  - PAID orders cannot be cancelled

#### 3. User Context
- **Aggregate Root**: `User`
- **Value Objects**: `UserId`, `UserRole`
- **Repository**: `UserRepository`

### Ubiquitous Language

Key domain concepts used consistently across code and documentation:
- **Cart**: Shopping basket for a user
- **Order**: Confirmed purchase request
- **Payment**: Financial transaction for an order
- **Idempotency**: Guarantee that duplicate requests produce the same result

---

## Layer Organization

### 1. Domain Layer (`domain/`)

**Purpose**: Contains pure business logic with no external dependencies.

**Structure**:
```
domain/
├── cart/
│   ├── Cart.java (Aggregate Root)
│   ├── CartId.java (Value Object)
│   ├── CartItem.java (Entity)
│   ├── CartRepository.java (Interface)
│   └── CartOwnershipPolicy.java (Domain Policy)
├── order/
│   ├── Order.java (Aggregate Root)
│   ├── OrderId.java (Value Object)
│   ├── OrderItem.java (Entity)
│   ├── OrderStatus.java (Enum)
│   ├── OrderRepository.java (Interface)
│   └── event/
│       ├── OrderCreatedEvent.java
│       └── OrderPaidEvent.java
├── user/
│   ├── User.java (Aggregate Root)
│   ├── UserId.java (Value Object)
│   └── UserRole.java (Enum)
└── exception/
    └── DomainException.java (Base exception)
```

**Key Characteristics**:
- No Spring annotations (except `@Document`, `@Id`, `@Version` for persistence mapping)
- Immutable value objects using Java records
- Business invariants enforced in constructors and methods
- Domain events for tracking important state changes

### 2. Application Layer (`application/`)

**Purpose**: Orchestrates domain objects to fulfill use cases.

**Structure**:
```
application/
├── cart/
│   ├── AddItemToCartUseCase.java
│   ├── UpdateCartItemQuantityUseCase.java
│   ├── RemoveItemFromCartUseCase.java
│   ├── ClearCartUseCase.java
│   ├── GetCartUseCase.java
│   ├── CartView.java (DTO)
│   └── CartItemView.java (DTO)
├── order/
│   ├── PlaceOrderFromCartUseCase.java
│   ├── PayOrderUseCase.java
│   ├── GetOrderUseCase.java
│   ├── OrderView.java (DTO)
│   └── OrderItemView.java (DTO)
├── security/
│   ├── CurrentUser.java (Interface)
│   ├── AccessDeniedException.java
│   └── AdminPolicy.java
└── event/
    └── DomainEventPublisher.java (Interface)
```

**Responsibilities**:
- Transaction management (`@Transactional`)
- Use case orchestration
- DTO mapping (domain → view)
- Security policy enforcement
- Event publishing

### 3. Infrastructure Layer (`infrastructure/`)

**Purpose**: Implements technical details and external integrations.

**Structure**:
```
infrastructure/
├── persistence/
│   ├── mongo/ (MongoDB implementations)
│   │   ├── cart/MongoCartRepository.java
│   │   └── order/MongoOrderRepository.java
│   └── memory/ (In-memory for testing)
│       ├── cart/InMemoryCartRepository.java
│       └── order/InMemoryOrderRepository.java
├── web/
│   ├── cart/CartController.java
│   ├── order/OrderController.java
│   └── ApiExceptionHandler.java
├── security/
│   ├── SecurityContextCurrentUser.java
│   └── CurrentUserArgumentResolver.java
├── outbox/
│   ├── OutboxEvent.java
│   ├── OutboxRepository.java
│   ├── OutboxEventPublisher.java
│   └── OutboxDomainEventPublisher.java
├── idempotency/
│   ├── IdempotencyKey.java
│   └── IdempotencyRepository.java
└── config/
    ├── CartConfig.java
    ├── OrderConfig.java
    ├── OutboxConfig.java
    └── WebConfig.java
```

---

## Key Design Patterns

### 1. Repository Pattern

**Purpose**: Abstract data access, allowing the domain to remain persistence-agnostic.

**Implementation**:
```java
// Domain interface
public interface CartRepository {
    Optional<Cart> findById(CartId id);
    void save(Cart cart);
    void delete(CartId id);
}

// Infrastructure implementation
@Repository
public class MongoCartRepository implements CartRepository {
    private final SpringDataCartRepository repository;
    // MongoDB-specific implementation
}
```

**Benefits**:
- Domain doesn't depend on MongoDB
- Easy to swap persistence technologies
- Simplified testing with in-memory implementations

### 2. Use Case Pattern

**Purpose**: Encapsulate application-specific business rules.

**Structure**:
```java
public class AddItemToCartUseCase {
    private final CartRepository cartRepository;

    @Transactional
    public CartView execute(String cartId, CurrentUser user, 
                           String productId, int quantity) {
        // 1. Load aggregate
        Cart cart = cartRepository.findById(new CartId(cartId))
                .orElseGet(() -> new Cart(new CartId(cartId), user.id()));
        
        // 2. Enforce security policy
        CartOwnershipPolicy.assertOwner(cart, user.id());
        
        // 3. Execute business logic
        cart.addItem(new ProductId(productId), quantity);
        
        // 4. Persist changes
        cartRepository.save(cart);
        
        // 5. Return DTO
        return CartView.from(cart);
    }
}
```

**Benefits**:
- Single Responsibility: One use case per class
- Testable without HTTP layer
- Clear transaction boundaries

### 3. Value Object Pattern

**Purpose**: Represent domain concepts with no identity, only value.

**Implementation**:
```java
public record CartId(String value) {
    public CartId {
        Objects.requireNonNull(value);
    }
}
```

**Benefits**:
- Immutability (thread-safe)
- Type safety (compile-time checks)
- Self-validating

### 4. Aggregate Pattern

**Purpose**: Ensure consistency boundaries in the domain model.

**Example**:
```java
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;
    private String ownerId;
    private Map<String, Integer> items = new HashMap<>();
    @Version
    private Long version; // Optimistic locking
    
    // Business methods enforce invariants
    public void addItem(ProductId productId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException(quantity);
        }
        items.merge(productId.value(), quantity, Integer::sum);
    }
}
```

**Characteristics**:
- **Root Entity**: `Cart` controls access to `CartItem`
- **Consistency Boundary**: All changes go through `Cart`
- **Optimistic Locking**: `@Version` prevents lost updates

### 5. Domain Events Pattern

**Purpose**: Decouple aggregates and enable event-driven workflows.

**Implementation**:
```java
public class Order {
    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    
    public void markAsPaid() {
        if (status != OrderStatus.CREATED) {
            throw new OrderAlreadyPaidException(getId());
        }
        this.status = OrderStatus.PAID;
        domainEvents.add(new OrderPaidEvent(getId()));
    }
    
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }
}
```

**Event Publishing**:
```java
@Transactional
public OrderView execute(String orderId, CurrentUser user, String idempotencyKey) {
    Order order = orderRepository.findById(new OrderId(orderId))
            .orElseThrow(() -> new OrderNotFoundException(orderId));
    
    order.markAsPaid();
    orderRepository.save(order);
    
    // Publish events after successful save
    order.pullDomainEvents().forEach(publisher::publish);
    
    return OrderView.from(order);
}
```

---

## Data Consistency & Concurrency

### Optimistic Locking

**Problem**: Prevent lost updates when multiple requests modify the same aggregate.

**Solution**: Use `@Version` annotation with MongoDB.

```java
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;
    
    @Version
    private Long version; // Incremented on each save
    
    // ...
}
```

**Behavior**:
1. User A reads Cart (version = 1)
2. User B reads Cart (version = 1)
3. User A saves Cart (version becomes 2)
4. User B tries to save Cart → `OptimisticLockingFailureException` (version mismatch)

**HTTP Response**: 409 Conflict with retry message.

### Idempotency

**Problem**: Duplicate payment requests could charge customers twice.

**Solution**: Idempotency keys ensure duplicate requests return the same response.

```java
@Transactional
public OrderView execute(String orderId, CurrentUser user, String idempotencyKey) {
    // Check if request was already processed
    return idempotencyRepository.findById(idempotencyKey)
            .map(this::deserialize)
            .orElseGet(() -> process(orderId, idempotencyKey));
}

private OrderView process(String orderId, String idempotencyKey) {
    Order order = orderRepository.findById(new OrderId(orderId))
            .orElseThrow(() -> new OrderNotFoundException(orderId));
    
    order.markAsPaid();
    orderRepository.save(order);
    order.pullDomainEvents().forEach(publisher::publish);
    
    OrderView view = OrderView.from(order);
    
    // Store result for future duplicate requests
    persistKey(idempotencyKey, view);
    
    return view;
}
```

**Usage**:
```http
POST /orders/order-123/pay
Idempotency-Key: unique-request-id-456

# Duplicate request with same key returns cached response
POST /orders/order-123/pay
Idempotency-Key: unique-request-id-456
```

---

## Event-Driven Architecture

### Transactional Outbox Pattern

**Problem**: Ensure events are published reliably when database commits.

**Solution**: Store events in database, publish asynchronously.

**Flow**:
```
1. Business Transaction
   ├─ Save aggregate (e.g., Order)
   └─ Save event to outbox table
   
2. Background Scheduler (every 3s)
   ├─ Query unpublished events
   ├─ Publish to message broker (Kafka/RabbitMQ)
   └─ Mark as published
```

**Implementation**:
```java
@Document(collection = "outbox_events")
public class OutboxEvent {
    @Id
    private String id;
    private String aggregateType;
    private String aggregateId;
    private String eventType;
    private String payload;
    private Instant occurredAt;
    private boolean published;
}

@Scheduled(fixedDelay = 3000)
@Transactional
public void publishPendingEvents() {
    List<OutboxEvent> events = repository.findByPublishedFalse();
    
    for (OutboxEvent event : events) {
        // Publish to Kafka/RabbitMQ
        log.info("Publishing event: {}", event.getEventType());
        
        event.markPublished();
        repository.save(event);
    }
}
```

**Benefits**:
- **Atomicity**: Events saved in same transaction as aggregate
- **Guaranteed Delivery**: Events persist until successfully published
- **Order Preservation**: Events published in order of creation

---

## Security & Authorization

### Role-Based Access Control (RBAC)

**Roles**:
- `CLIENT`: Standard user (can manage own carts/orders)
- `ADMIN`: Administrator (can access all resources)

**Policy Enforcement**:

```java
// Domain policy
public final class CartOwnershipPolicy {
    public static void assertOwner(Cart cart, UserId userId) {
        if (!cart.getOwnerId().equals(userId)) {
            throw new AccessDeniedException();
        }
    }
}

// Application policy
public final class AdminPolicy {
    public static void assertAdmin(CurrentUser currentUser) {
        if (!currentUser.hasRole(UserRole.ADMIN)) {
            throw new AccessDeniedException();
        }
    }
}
```

**Usage in Use Cases**:
```java
public CartView execute(String cartId, CurrentUser user, String productId, int quantity) {
    Cart cart = cartRepository.findById(new CartId(cartId))
            .orElseGet(() -> new Cart(new CartId(cartId), user.id()));
    
    // Enforce ownership
    CartOwnershipPolicy.assertOwner(cart, user.id());
    
    cart.addItem(new ProductId(productId), quantity);
    cartRepository.save(cart);
    
    return CartView.from(cart);
}
```

### CurrentUser Abstraction

**Interface**:
```java
public interface CurrentUser {
    UserId id();
    UserRole role();
    boolean hasRole(UserRole role);
}
```

**Implementations**:
- `SecurityContextCurrentUser`: Extracts user from Spring Security context
- `FixedCurrentUser`: Test implementation with hardcoded user

**Controller Integration**:
```java
@PostMapping("/{cartId}/items")
public CartResponse add(
    @PathVariable String cartId,
    @RequestBody AddItemRequest request,
    CurrentUser currentUser  // Injected by CurrentUserArgumentResolver
) {
    return CartResponse.from(
        addItem.execute(cartId, currentUser, request.productId(), request.quantity())
    );
}
```

---

## Testing Strategy

### Test Pyramid

```
        ┌─────────────┐
        │     E2E     │  (Integration/Controller Tests)
        ├─────────────┤
        │  Use Cases  │  (Application Layer Tests)
        ├─────────────┤
        │   Domain    │  (Unit Tests - Most Coverage)
        └─────────────┘
```

### 1. Domain Layer Tests

**Focus**: Business logic and invariants.

**Characteristics**:
- Pure unit tests
- No mocks needed
- Fast execution

**Example**:
```java
@Test
void shouldIncreaseQuantityWhenAddingSameProduct() {
    Cart cart = new Cart(new CartId("cart-1"), new UserId("user-1"));
    ProductId pid = new ProductId("product-1");
    
    cart.addItem(pid, 2);
    cart.addItem(pid, 3);
    
    CartItem item = cart.getItems().get(pid);
    assertEquals(5, item.getQuantity());
}

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

### 2. Application Layer Tests

**Focus**: Use case orchestration and event publishing.

**Characteristics**:
- In-memory repositories
- Fake event publishers
- No HTTP layer

**Example**:
```java
@Test
void shouldPlaceOrderFromCartAndClearCart() {
    InMemoryCartRepository cartRepository = new InMemoryCartRepository();
    InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
    FakeDomainEventPublisher publisher = new FakeDomainEventPublisher();
    
    Cart cart = new Cart(new CartId("cart-1"), new UserId("user-1"));
    cart.addItem(new ProductId("product-1"), 2);
    cartRepository.save(cart);
    
    PlaceOrderFromCartUseCase useCase = new PlaceOrderFromCartUseCase(
        cartRepository, orderRepository, publisher
    );
    
    useCase.execute("cart-1", new FixedCurrentUser("user-1"));
    
    // Verify event published
    assertEquals(1, publisher.count());
    assertTrue(publisher.first() instanceof OrderCreatedEvent);
    
    // Verify cart cleared
    Cart persistedCart = cartRepository.findById(new CartId("cart-1")).orElseThrow();
    assertTrue(persistedCart.isEmpty());
}
```

### 3. Controller Tests

**Focus**: HTTP integration and request/response mapping.

**Characteristics**:
- `@WebMvcTest` for focused controller testing
- Mock security context
- Validate HTTP status codes and JSON responses

**Example**:
```java
@WebMvcTest(controllers = CartController.class)
@Import(CartControllerTest.TestConfig.class)
public class CartControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void shouldAddItemToCart() throws Exception {
        String cartId = "cart-1";
        cartRepository.save(new Cart(new CartId(cartId), new UserId(userId)));
        
        mockMvc.perform(post("/carts/{cartId}/items", cartId)
                .with(user(userId))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\": \"product-1\", \"quantity\": 2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId));
    }
}
```

### Test Utilities

**In-Memory Repositories**:
```java
public class InMemoryCartRepository implements CartRepository {
    private final Map<String, Cart> storage = new ConcurrentHashMap<>();
    
    @Override
    public Optional<Cart> findById(CartId id) {
        return Optional.ofNullable(storage.get(id.value()));
    }
    
    public void clear() {
        storage.clear();
    }
}
```

**Fake Event Publisher**:
```java
class FakeDomainEventPublisher implements DomainEventPublisher {
    private final List<DomainEvent> events = new ArrayList<>();
    
    @Override
    public void publish(DomainEvent event) {
        events.add(event);
    }
    
    int count() { return events.size(); }
    DomainEvent first() { return events.get(0); }
}
```

---

## Error Handling

### Exception Hierarchy

```
RuntimeException
└── DomainException (Business errors)
    ├── CartNotFoundException
    ├── OrderNotFoundException
    ├── EmptyCartException
    ├── InvalidQuantityException
    ├── OrderAlreadyPaidException
    └── PaidOrderCannotBeCancelledException

RuntimeException
└── AccessDeniedException (Security errors)
```

### Global Exception Handler

```java
@RestControllerAdvice
public class ApiExceptionHandler {
    
    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(DomainException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Business rule violation");
        problem.setDetail(ex.getMessage());
        return problem;
    }
    
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ProblemDetail handleOptimisticLocking(OptimisticLockingFailureException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Concurrent modification");
        problem.setDetail("The resource was modified by another request. Please retry.");
        return problem;
    }
}
```

**HTTP Responses**:
- `400 Bad Request`: Domain exceptions (business rule violations)
- `403 Forbidden`: Access denied (authorization failures)
- `404 Not Found`: Resource not found
- `409 Conflict`: Optimistic locking failures
- `500 Internal Server Error`: Unexpected errors

---

## Technology Stack

### Core Technologies
- **Java 17+**: Modern Java with records and pattern matching
- **Spring Boot 3.x**: Application framework
- **Spring Data MongoDB**: Persistence layer
- **Spring Security**: Authentication and authorization

### Data & Persistence
- **MongoDB**: Document database for aggregates
- **Optimistic Locking**: Concurrency control via `@Version`

### Testing
- **JUnit 5**: Test framework
- **MockMvc**: Controller testing
- **In-memory repositories**: Fast, isolated tests

### Build & Dependency Management
- **Gradle/Maven**: Build automation
- **Jakarta Validation**: Request validation (`@Valid`, `@NotBlank`, `@Min`)

---

## Deployment Considerations

### Scalability

**Stateless Application**:
- No session state stored in memory
- All state persists in MongoDB
- Horizontal scaling via load balancer

**Event Processing**:
- Outbox pattern ensures at-least-once delivery
- Idempotent event handlers prevent duplicate processing

### Monitoring & Observability

**Recommended Additions**:
- Distributed tracing (e.g., OpenTelemetry)
- Metrics collection (e.g., Micrometer)
- Centralized logging (e.g., ELK stack)
- Health checks for MongoDB and message broker

### Configuration

**Environment-Specific**:
- Database connection strings
- Message broker endpoints
- Security configurations
- Logging levels

---

## Future Enhancements

### Immediate Priorities
1. **CQRS**: Separate read/write models for complex queries
2. **Event Sourcing**: Store events as source of truth
3. **Saga Pattern**: Distributed transactions across aggregates
4. **API Gateway**: Centralized routing and authentication

### Long-Term Goals
1. **Microservices**: Split bounded contexts into separate services
2. **Event Store**: Dedicated storage for domain events
3. **Read Models**: Optimized projections for queries
4. **GraphQL API**: Flexible query interface

---

## References

- **Clean Architecture** by Robert C. Martin
- **Domain-Driven Design** by Eric Evans
- **Implementing Domain-Driven Design** by Vaughn Vernon
- **Enterprise Integration Patterns** by Gregor Hohpe
- **Building Microservices** by Sam Newman
