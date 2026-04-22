---
name: api-endpoint
description: Generate a REST API endpoint for this service. Use when asked to add an endpoint, create a controller method, or expose a new use case via HTTP.
allowed-tools: Read, Write
---

When generating a REST endpoint, always create the full vertical slice:

1. Read existing controllers in src/main/java/com/example/inventory/api/ to match patterns.
2. Read existing application services in src/main/java/com/example/inventory/application/ to match patterns.

Vertical slice order (create in this sequence):
  a. Command or Query record in application/ (the input DTO)
  b. Response record in application/ (the output DTO)  
  c. Method on the ApplicationService in application/
  d. Controller method in api/ that delegates to the service
  e. Integration test using @SpringBootTest + MockMvc

REST conventions:
- POST   /resource          → create, returns 201 + Location header
- GET    /resource/{id}     → fetch by id, returns 200 or 404
- PUT    /resource/{id}     → full update, returns 200
- PATCH  /resource/{id}     → partial update, returns 200
- DELETE /resource/{id}     → remove, returns 204

Controller template:
@RestController
@RequestMapping("/api/v1/{resource}")
@RequiredArgsConstructor
public class {Resource}Controller {
    private final {Resource}ApplicationService service;

    @PostMapping
    public ResponseEntity<{Response}> create(@Valid @RequestBody {Command} cmd) {
        var result = service.handle(cmd);
        return ResponseEntity.created(URI.create("/api/v1/{resource}/" + result.id())).body(result);
    }
}

Never put try-catch in controllers — use a @RestControllerAdvice for exception handling.
