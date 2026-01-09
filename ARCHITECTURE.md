# Architecture Overview

This document describes the architectural principles, boundaries, and testing strategy of the **ecomm-app-java** backend.

The goal of this architecture is to keep the codebase **coherent, explicit, and resilient to change**, even as the system grows in complexity.

---

## 1. Architectural Style

The project follows a **DDD-inspired, layered architecture** with strict dependency direction:

domain
↑
application
↑
infrastructure

### Core rule

> **Dependencies always point inward.**  
> Outer layers may depend on inner layers, never the opposite.

---

## 2. Layer Responsibilities

### 2.1 Domain Layer (`com.example.ecommerce.domain`)

**Purpose:**  
Represents the core business model and rules.

**Contains:**
- Aggregates (`Order`, `Cart`, etc.)
- Value objects (`OrderId`, `ProductId`, etc.)
- Domain exceptions
- Domain events

**Must NOT depend on:**
- Spring
- Security
- Persistence
- Web / HTTP
- Application services

**Key rule:**
> The domain must be usable in complete isolation from frameworks.

---

### 2.2 Application Layer (`com.example.ecommerce.application`)

**Purpose:**  
Orchestrates business use cases and enforces application-level policies.

**Contains:**
- Use cases (e.g. `AddItemToCartUseCase`)
- Application policies (e.g. `AdminPolicy`)
- Ports / gateways (`ProductGateway`)
- Application-level abstractions (`CurrentUser`)

**Depends on:**
- Domain only

**Must NOT depend on:**
- Spring MVC
- Spring Security
- Persistence implementations
- Web DTOs

**Key rule:**
> The application layer defines *what the system does*, not *how it is delivered*.

---

### 2.3 Infrastructure Layer (`com.example.ecommerce.infrastructure`)

**Purpose:**  
Connects the application to the outside world.

**Contains:**
- Web controllers
- Persistence adapters (Mongo, in-memory, etc.)
- Security adapters
- Framework configuration
- Web DTOs (`OrderView`, `CartView`, etc.)

**Allowed to depend on:**
- Domain
- Application
- Frameworks (Spring, Mongo, Security, etc.)

**Key rule:**
> Infrastructure absorbs all technical complexity so inner layers remain clean.

---

## 3. Web Layer Design

### 3.1 Controllers

Controllers are **thin** and responsible only for:

- HTTP routing
- Request/response mapping
- Translating exceptions into HTTP responses
- Extracting the current user from security context

Controllers **do not**:
- Contain business logic
- Manipulate domain objects directly
- Access repositories

### 3.2 HTTP Semantics

All controller methods explicitly declare HTTP intent.

#### Command endpoints
- Return `void`
- Declare status explicitly

Examples:
- `POST` → `201 CREATED`
- `PUT / DELETE` → `204 NO_CONTENT`

#### Query endpoints
- Return DTOs
- Default to `200 OK`

**Rule:**
> No controller relies on Spring’s default HTTP status behavior.

---

## 4. Security Architecture

### 4.1 Abstraction

Security is abstracted via the `CurrentUser` interface in the **application layer**.

```java
public interface CurrentUser {
    String id();
    UserRole role();
    boolean hasRole(UserRole role);
}
4.2 Infrastructure Adapter
SecurityContextCurrentUser lives in:
infrastructure.security
It translates Spring Security’s Authentication into CurrentUser.
Rule:
Spring Security types never appear outside infrastructure.

5. Persistence Strategy
    • Repositories are interfaces in the domain or application layer
    • Implementations live in infrastructure
    • In-memory repositories are used for tests
    • Test-only helpers (e.g. findAll()) are allowed only in in-memory implementations
Rule:
Repository interfaces expose only business-meaningful operations.

6. Testing Strategy
6.1 MVC Tests (@WebMvcTest)
Purpose:
    • Verify HTTP contracts
    • Verify routing, status codes, and JSON shapes
    • Verify exception → HTTP mapping
Characteristics:
    • Use cases are mocked
    • No domain state assertions
    • CSRF tokens are explicitly added for write operations
    • @WithMockUser is used for authentication
Rule:
MVC tests test translation, not business behavior.

6.2 Use Case Tests
Purpose:
    • Verify business behavior
    • Verify domain state changes
    • Verify domain events
Characteristics:
    • No Spring
    • No MVC
    • Use in-memory repositories

6.3 Integration Tests (future)
Purpose:
    • End-to-end confidence
    • Full Spring context
    • Real persistence
Not required for day-to-day development.

7. DTO vs Domain Model
    • Domain models are never serialized directly
    • Controllers return web DTOs (*View)
    • DTOs expose only what the API contract guarantees
Rule:
Tests must reflect DTOs, not domain internals.

8. Evolution Guidelines
When adding new features:
    1. Start in the domain
    2. Add orchestration in application
    3. Adapt delivery in infrastructure
    4. Update tests at the appropriate level
If a change causes:
    • many MVC tests to break → HTTP contract changed
    • many use-case tests to break → business rule changed
    • domain tests to break → invariant changed
This signal is intentional and valuable.

9. Final Principle
Clarity beats cleverness.
Explicit boundaries, explicit contracts, and explicit tests keep the system healthy as it grows.


