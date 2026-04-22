---
name: ddd-entity
description: Generate a DDD aggregate root, entity, or value object for this project. Use when asked to create domain models, aggregates, entities, or value objects.
allowed-tools: Read, Write
---

When generating a DDD entity or aggregate:

1. Read the existing files in src/main/java/com/example/inventory/domain/ to understand current patterns before writing anything.
2. For value objects: use a Java record with validation in the compact constructor.
3. For aggregate roots: use a class with private constructor and a static factory method create() that enforces invariants.
4. Place domain events as records in the same package as the aggregate.
5. Never use @Entity or any JPA annotation — those belong in infrastructure.
6. Immediately write a unit test in the matching package under src/test/java using plain JUnit 5 (no Spring context).

Aggregate template:
public class {Name} {
    private final {Name}Id id;

    private {Name}(...) { }

    public static {Name} create(...) {
        // validate invariants here
        return new {Name}(...);
    }
}

Value object template:
public record {Name}({Type} value) {
    public {Name} {
        // validate in compact constructor
    }
}
