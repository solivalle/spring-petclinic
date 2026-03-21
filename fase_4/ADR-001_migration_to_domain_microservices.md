# ADR-001 - Migration to Domain Microservices

## Status

Proposed

## Date

2026-03-21

## Context

### Current architecture evidence

The current system is a modular monolith with a single runtime and shared schema:

| Evidence | Repository source | Observation |
|---|---|---|
| Single deployable application | `src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java` | One Spring Boot application bootstraps all domains. |
| Domain packages in one codebase | `src/main/java/org/springframework/samples/petclinic/{owner,vet,system}` | Logical boundaries exist, but runtime boundary is shared. |
| Shared relational model | `src/main/resources/db/postgres/schema.sql` | `owners`, `pets`, `visits`, `vets`, `specialties` live in one schema script. |
| Shared deployment unit in k8s | `k8s/petclinic.yml` | One deployment (`replicas: 1`) serves all capabilities. |
| Web and data concerns coupled per feature | `OwnerController.java`, `VetController.java` | Controllers still orchestrate repository access directly in key flows. |

### Problem

Current architecture supports moderate scope, but creates strategic constraints:

- release coupling across domains,
- no independent scaling per domain,
- high blast radius for runtime incidents,
- shared database ownership bottlenecks.

### Architectural drivers

- Improve domain autonomy and team ownership.
- Reduce blast radius during incidents.
- Enable domain-level scaling and independent release cadence.
- Build migration path from current Strangler Fig refactoring efforts.

## Decision

Adopt a staged migration from modular monolith to domain microservices.

### Target state

- `owner-service`: owners, pets, visits, pet types.
- `vet-service`: vets, specialties.
- `clinic-web` (transitional): web composition and backward-compatible routing.
- Database per service (logical split first, physical split by phase).
- API gateway for edge traffic and compatibility routing.
- Events for asynchronous cross-domain propagation when eventual consistency is acceptable.

### Alternatives considered

1. Keep monolith and continue internal refactoring only.
2. Migrate to domain microservices (selected).
3. Full rewrite from scratch.

Option 2 is selected because it balances strategic benefit and migration risk better than the alternatives.

## Consequences

### Trade-offs

| Area | Positive consequence | Negative consequence |
|---|---|---|
| Deployment | Independent domain releases | More CI/CD pipelines and release governance |
| Scalability | Scale hot domains only | Service-by-service capacity planning overhead |
| Reliability | Better fault isolation | More network failure modes |
| Data | Clear ownership boundaries | Eventual consistency and data duplication management |
| Team topology | Stronger domain ownership | Requires platform/SRE maturity |

### Risks and mitigations

| Risk | Probability | Impact | Mitigation |
|---|---|---|---|
| Data inconsistency across services | Medium | High | Outbox/idempotency/reconciliation jobs |
| Latency increase due to remote calls | Medium | Medium | API budgets, caching, pagination, denormalized read models |
| Functional regressions during migration | Medium | High | Strangler routing, parity tests, canary releases |
| Team overload from platform complexity | High | Medium | Shared platform templates and enablement backlog |
| Security surface expansion | Medium | High | Service authn/authz baseline and secret lifecycle controls |

### Cost estimate

| Cost component | Estimate |
|---|---|
| One-time implementation | 38-56 engineer-weeks |
| Recurring operational delta | USD 550-1,600 per month |
| Indirect cost | Upskilling, on-call maturity, API contract governance |

### Implementation phases

1. Foundations: service boundaries, contracts, observability baseline.
2. Pilot extraction: `vet-service`.
3. Core extraction: `owner-service`.
4. Data ownership hardening and legacy path decommission.

### Expected outcomes

- 80%+ of domain changes deployable without full monolith release.
- 30% MTTR reduction for domain incidents.
- P95 latency within service-level budgets after extraction.
