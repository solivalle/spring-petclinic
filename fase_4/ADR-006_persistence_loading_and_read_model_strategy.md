# ADR-006 - Persistence Loading and Read Model Strategy

## Status

Proposed

## Date

2026-03-21

## Context

### Current query/loading evidence

Current domain mappings and query flows show potential read-scaling bottlenecks:

| Evidence | Repository source | Observation |
|---|---|---|
| EAGER owner-pet loading | `src/main/java/org/springframework/samples/petclinic/owner/Owner.java` | `@OneToMany(... fetch = FetchType.EAGER)` on pets collection. |
| EAGER pet-visit loading | `src/main/java/org/springframework/samples/petclinic/owner/Pet.java` | `@OneToMany(... fetch = FetchType.EAGER)` on visits collection. |
| Controller-driven read flows | `OwnerController.java`, `VisitController.java` | View flows rely on full object graphs in request scope. |
| Open Session in View disabled | `src/main/resources/application.properties` | `spring.jpa.open-in-view=false`, forcing explicit data loading discipline. |
| Cache applied to limited domain | `VetRepository.java`, `CacheConfiguration.java` | Cache mostly optimizes vet reads, not owner/pet heavy paths. |

### Problem

EAGER graph loading can increase memory and query cost as data volume grows:

- unnecessary data transfer for simple views,
- higher risk of N+1 and over-fetching behavior,
- reduced flexibility for domain-specific read optimization.

### Architectural drivers

- Improve read performance predictability.
- Align persistence strategy with service extraction and API contracts.
- Reduce coupling between write model and read model concerns.

## Decision

Adopt LAZY-first entity loading plus explicit read models for query use cases.

### Decision details

- Move relationship defaults from EAGER to LAZY where safe.
- Introduce query-specific projections/DTOs for list and detail screens.
- Use repository methods with explicit fetch strategies (`join fetch`, projections).
- Add targeted caching for high-read endpoints beyond vets.

### Alternatives considered

1. Keep current EAGER mappings.
2. LAZY + read model strategy (selected).
3. Full CQRS split in a single step.

Option 2 is selected as an incremental path with lower delivery risk than full CQRS adoption.

## Consequences

### Trade-offs

| Area | Positive consequence | Negative consequence |
|---|---|---|
| Performance | Lower over-fetching and better query control | More repository/query design effort |
| Maintainability | Explicit read contracts and better testability | Additional DTO/projection classes |
| Scalability | Better fit for microservice extraction | Requires strong query observability |
| Delivery | Incremental adoption possible | Temporary mixed loading patterns during migration |

### Risks and mitigations

| Risk | Probability | Impact | Mitigation |
|---|---|---|---|
| Lazy initialization errors in existing flows | Medium | High | Service-layer transaction boundaries and integration tests |
| Query regressions after fetch strategy changes | Medium | High | SQL profiling and performance baseline tests |
| DTO explosion and code overhead | Medium | Medium | Shared mapping conventions and code generation where justified |

### Cost estimate

| Cost component | Estimate |
|---|---|
| One-time implementation | 7-12 engineer-weeks |
| Recurring operational delta | -USD 50 to -USD 300 per month (query/load efficiency gains) |
| Indirect cost | Repository refactors, query observability dashboards |

### Expected outcomes

- Better P95 read latency stability for owner/pet/visit use cases.
- Lower database and JVM memory pressure under growth.
- Cleaner separation between write entities and read APIs.
