# E-Commerce Backend (Java / Spring)

This repository is a **showcase backend application** built with Java and Spring, designed to demonstrate **clean architecture, DDD principles, and robust testing practices** in a real-world setting.

Rather than focusing on feature breadth, the project emphasizes **clarity of design, explicit boundaries, and correctness under change**.

---

## 🧱 Architecture Overview

The system follows a strict layered architecture inspired by **Domain-Driven Design**:

domain
↑
application
↑
infrastructure

### Key characteristics

- Clear dependency direction (outer layers depend on inner layers only)
- Framework-agnostic domain and application layers
- All Spring and technical concerns isolated in infrastructure
- Explicit HTTP contracts and error handling

For full details, see [`ARCHITECTURE.md`](ARCHITECTURE.md).

---

## 📦 Modules & Responsibilities

### Domain
- Business aggregates (`Order`, `Cart`)
- Value objects (`OrderId`, `ProductId`)
- Domain rules and invariants
- Domain events

### Application
- Use cases (e.g. *AddItemToCart*, *PlaceOrderFromCart*)
- Application-level policies (e.g. admin authorization)
- Security abstraction (`CurrentUser`)
- Ports/gateways for external contexts

### Infrastructure
- REST controllers (Spring MVC)
- Persistence adapters (MongoDB, in-memory)
- Security integration (Spring Security)
- Web DTOs and mappers
- Configuration and exception handling

---

## 🔐 Security Model

- Authentication and authorization are handled via **Spring Security**
- Business logic depends on a framework-agnostic `CurrentUser` abstraction
- Controllers translate Spring’s `Authentication` into `CurrentUser`
- No Spring Security types leak into domain or application layers

CSRF protection is enabled and explicitly handled in MVC tests.

---

## 🌐 API Design

- Commands and queries are clearly separated
- Command endpoints:
  - Return no body
  - Declare intent explicitly (`201 CREATED`, `204 NO_CONTENT`)
- Query endpoints:
  - Return DTOs
  - Default to `200 OK`

Controllers are thin and delegate all business logic to use cases.

---

## 🧪 Testing Strategy

The project uses **layered testing**, with each test type having a single responsibility.

### MVC Tests (`@WebMvcTest`)
- Verify HTTP contracts
- Validate status codes and JSON shapes
- Mock application use cases
- Explicit CSRF handling
- No domain state assertions

### Use Case Tests
- Verify business behavior
- Validate domain state changes
- Use in-memory repositories
- No Spring context

### Integration Tests (future)
- Full application wiring
- Real persistence
- End-to-end validation

---

## 🚀 Running the Project

### Requirements
- Java 21+
- Gradle

### Run tests

```bash
./gradlew test
Run the application
./gradlew bootRun

📄 Project Goals
This repository is intentionally designed to:
    • Serve as a reference architecture
    • Demonstrate how to introduce security without breaking the domain model
    • Show how tests can drive clarity rather than fight the framework
    • Remain maintainable as complexity grows
It is not intended to be a production-ready e-commerce platform, but a clean and realistic backend example.

📘 Further Reading
    • ARCHITECTURE.md — detailed architectural decisions
    • DDD (Evans)
    • Clean Architecture (Robert C. Martin)

🧠 Final Note
This codebase prioritizes explicitness over cleverness and correctness over shortcuts.
If you are reviewing this repository, focus on:
    • dependency direction
    • test intent
    • separation of concerns
    • clarity of contracts
Those choices are deliberate.

