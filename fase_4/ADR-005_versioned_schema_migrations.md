# ADR-005 - Versioned Schema Migrations with Flyway

## Status

Proposed

## Date

2026-03-21

## Context

### Current schema management evidence

Database lifecycle is currently based on SQL initialization scripts by profile:

| Evidence | Repository source | Observation |
|---|---|---|
| SQL init properties | `src/main/resources/application.properties` | Uses `spring.sql.init.schema-locations` and `data-locations`. |
| Profile-based schema scripts | `src/main/resources/db/{h2,mysql,postgres}/schema.sql` | Multiple schema definitions are maintained in parallel. |
| Profile-based seed scripts | `src/main/resources/db/{h2,mysql,postgres}/data.sql` | Data scripts differ by engine and constraints. |
| Runtime profile switching | `application-mysql.properties`, `application-postgres.properties` | Initialization behavior changes with selected database profile. |

### Problem

Script-based initialization is adequate for bootstrap, but weak for long-lived change governance:

- limited traceability of schema evolution,
- harder rollback/forward control,
- higher risk of drift between environments.

### Architectural drivers

- Require auditable and ordered schema evolution.
- Improve release safety for incremental data model changes.
- Support multi-stage environments with deterministic migration state.

## Decision

Adopt Flyway as the schema migration mechanism for non-local environments.

### Decision details

- Introduce versioned migration scripts (`V1__...`, `V2__...`) as source of truth.
- Reserve SQL init bootstrap for local/testing convenience only when required.
- Enforce migration execution as part of deployment pipeline.
- Track migration status and failures in observability dashboards.

### Alternatives considered

1. Keep only `spring.sql.init.*` script strategy.
2. Adopt Flyway versioned migrations (selected).
3. Adopt Liquibase changelog strategy.

Option 2 is selected for simple SQL-first workflow aligned with current repository style.

## Consequences

### Trade-offs

| Area | Positive consequence | Negative consequence |
|---|---|---|
| Governance | Full migration history and ordering | Requires migration discipline in every release |
| Reliability | Safer rollout/rollback planning | Bad migration scripts can block deploys |
| Team workflow | Clear DB change process | Extra review steps for schema PRs |
| Operations | Better env parity tracking | Initial setup and CI integration effort |

### Risks and mitigations

| Risk | Probability | Impact | Mitigation |
|---|---|---|---|
| Faulty migration blocks deployment | Medium | High | Pre-prod migration rehearsals and backup checkpoints |
| Drift between old init scripts and new migrations | Medium | Medium | Freeze policy and migration cutover checklist |
| Performance impact of large DDL changes | Medium | High | Expand/contract strategy and maintenance windows |

### Cost estimate

| Cost component | Estimate |
|---|---|
| One-time implementation | 5-9 engineer-weeks |
| Recurring operational delta | +USD 0-150 per month (observability and pipeline minutes) |
| Indirect cost | Training and migration review governance |

### Expected outcomes

- Deterministic schema versioning across environments.
- Lower production risk for database change deployments.
- Better auditability for compliance and incident analysis.
