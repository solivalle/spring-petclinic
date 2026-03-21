# ADR-003 - API Gateway and Contract-First Edge Strategy

## Status

Proposed

## Date

2026-03-21

## Context

### Current API/edge evidence

Current endpoints and web concerns are mixed in the same runtime and lack explicit API governance:

| Evidence | Repository source | Observation |
|---|---|---|
| HTML and resource endpoints coexist | `VetController.java` | `/vets.html` (HTML) and `/vets` (resource) are served from same controller. |
| Owner flows are server-rendered routes | `OwnerController.java`, `PetController.java`, `VisitController.java` | UI routing and domain orchestration are tightly coupled. |
| No API versioning convention | controllers and route definitions | Endpoints do not expose explicit `/api/v1` strategy. |
| Planned microservice migration | `fase4/ADR-001_migration_to_domain_microservices.md` | Service extraction needs edge routing and compatibility control. |

### Problem

Without a gateway and explicit API contracts, service extraction will face:

- route fragmentation and backward compatibility risk,
- inconsistent API behavior across domains,
- limited observability and policy enforcement at the edge.

### Architectural drivers

- Protect backward compatibility during Strangler migration.
- Standardize API contracts and versioning.
- Centralize cross-cutting concerns: auth, rate limiting, telemetry, and routing rules.

## Decision

Introduce an API Gateway and adopt a contract-first, versioned API standard.

### Decision details

- Add gateway as the single edge for API traffic.
- Define API namespace/version baseline (`/api/v1/...`) for machine consumers.
- Keep legacy routes available behind gateway-managed compatibility rules.
- Add contract tests for producer/consumer compatibility.
- Enforce edge policies (authn/authz, rate limit, request correlation IDs).

### Alternatives considered

1. Keep direct service exposure without gateway.
2. Gateway + contract-first strategy (selected).
3. Service mesh only, no explicit API edge governance.

Option 2 is selected because it addresses compatibility, governance, and migration control together.

## Consequences

### Trade-offs

| Area | Positive consequence | Negative consequence |
|---|---|---|
| Compatibility | Controlled migration and rollback at route level | Additional edge infrastructure to manage |
| Governance | Unified policy enforcement and API standards | Initial overhead to define and maintain contracts |
| Observability | Better request tracing across boundaries | New operational layer introduces extra failure points |
| Developer experience | Clear entry point and API lifecycle process | Teams must adopt stricter API review discipline |

### Risks and mitigations

| Risk | Probability | Impact | Mitigation |
|---|---|---|---|
| Gateway becomes bottleneck or SPOF | Medium | High | Horizontal scaling, health checks, and fallback routing |
| Contract drift between teams | Medium | High | Mandatory contract tests in CI and change approval workflow |
| Migration stalls due to legacy endpoint complexity | Medium | Medium | Prioritized endpoint inventory and phased route cutover |
| Latency overhead at edge | Medium | Medium | Performance budgets and lightweight policy chain |

### Cost estimate

| Cost component | Estimate |
|---|---|
| One-time implementation | 10-16 engineer-weeks |
| Recurring operational delta | USD 200-700 per month |
| Indirect cost | API governance process, gateway operations training |

### Expected outcomes

- Stable migration path with route-level control.
- Better external API consistency and lifecycle management.
- Stronger observability and policy enforcement at system boundaries.
