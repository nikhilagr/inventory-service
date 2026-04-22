# Inventory Service

## Project

A Spring Boot 3.x microservice responsible for managing product inventory — stock levels, reservations, and adjustments. Built with Java 21 and structured around Domain-Driven Design.

Base package: `com.example.inventory`

## Architecture

The codebase follows a strict layered DDD structure:

```
src/main/java/com/example/inventory/
├── domain/          # Core business logic — entities, value objects, domain events, repository interfaces, domain services
├── application/     # Use-case orchestration — application services, commands, queries, DTOs
├── infrastructure/  # Technical implementations — JPA repositories, messaging adapters, external clients
└── api/             # Inbound adapters — REST controllers, request/response models, exception handlers
```

**domain** — no framework dependencies; contains entities, aggregates, value objects, domain events, domain services, and repository interfaces (`InventoryRepository`, etc.). Must not import from any other layer.

**application** — orchestrates domain objects to fulfill use cases. Holds application services (command/query handlers), DTOs, and mappers. The only layer annotated with `@Transactional`. Must not contain business logic.

**infrastructure** — implements domain interfaces using Spring Data JPA, Hibernate, messaging (Kafka/SQS), and other frameworks. JPA entity mappings live here, separate from domain aggregates.

**api** — thin REST layer. Controllers delegate directly to application services. No logic beyond request validation and response mapping.

## Coding Standards

- **Java 21**: use records for DTOs and value objects, sealed classes + pattern matching for domain variants, `switch` expressions over `if-else` chains where appropriate.
- `@Transactional` goes on application service methods only — never on controllers or domain objects.
- Repository interfaces are defined in `domain`; JPA implementations live in `infrastructure`.
- No Lombok — use records, explicit constructors, and accessor methods.
- Prefer `Optional` over null returns on domain finders.
- Domain events are plain Java records; publishing happens in the application layer after a successful transaction.

## Tests

- Unit tests: JUnit 5, plain instantiation — no Spring context, no mocks of domain objects.
- Integration tests: JUnit 5 + Testcontainers (PostgreSQL, Kafka as needed); use `@SpringBootTest` sparingly.
- Test classes mirror the source package structure under `src/test/java`.

## Maven Commands

```bash
# Build (skip tests)
mvn clean package -DskipTests

# Run all tests
mvn verify

# Run the service locally
mvn spring-boot:run

# Checkstyle
mvn checkstyle:check
```

## What NOT to Do

- No business logic in controllers (`api` layer) — validation only.
- No business logic in application services — they orchestrate, not decide.
- `domain` must never import from `infrastructure` or `api`; enforce this with ArchUnit if available.
- No static utility methods that encode domain rules — put invariants on the aggregate itself.
- Do not expose JPA entities (`@Entity`) outside the `infrastructure` layer; map to domain objects at the repository implementation boundary.
