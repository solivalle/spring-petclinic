# ADR-002 - Database Standardization to PostgreSQL

## Status

Proposed

## Date

2026-03-21

## Context

### Current persistence evidence

The repository currently supports multiple database engines with divergent runtime paths:

| Evidence | Repository source | Observation |
|---|---|---|
| Default profile points to H2 path | `src/main/resources/application.properties` | `database=h2` and shared SQL init properties. |
| Dedicated MySQL profile | `src/main/resources/application-mysql.properties` | Separate connection settings and init behavior. |
| Dedicated Postgres profile | `src/main/resources/application-postgres.properties` | Separate connection settings and init behavior. |
| Runtime manifests favor Postgres | `k8s/db.yml`, `k8s/petclinic.yml` | Kubernetes deployment and secret wiring are Postgres-oriented. |
| Local compose starts MySQL and Postgres | `docker-compose.yml` | Two DB engines increase local and CI matrix complexity. |
| Integration tests include MySQL and Postgres | `MySqlIntegrationTests.java`, `PostgresIntegrationTests.java` | Dual engine support increases test maintenance cost. |

### Problem

Maintaining multiple first-class relational engines increases:

- operational and CI complexity,
- schema drift risk across engines,
- validation and troubleshooting effort per release.

### Architectural drivers

- Reduce operational variance with a paved-road persistence strategy.
- Align CI and production semantics.
- Lower total cost of ownership for database evolution and incident handling.

## Decision

Standardize PostgreSQL as the strategic database for all non-local environments.

### Decision details

- Keep Postgres as production and staging standard.
- Keep H2 for fast local/unit scenarios only.
- Deprecate MySQL as a first-class runtime target in phased manner.
- Move integration pipeline default to Postgres contract tests.
- Keep a temporary compatibility window for MySQL while migration backlog is executed.

### Alternatives considered

1. Preserve dual-engine strategy (MySQL + Postgres) for long term.
2. Standardize on PostgreSQL (selected).
3. Standardize on MySQL.

Option 2 is selected because deployment artifacts and operational direction are already Postgres-centered.

## Consequences

### Trade-offs

| Area | Positive consequence | Negative consequence |
|---|---|---|
| Operability | Fewer runtime variants and simpler troubleshooting | Reduced engine portability |
| Quality assurance | Lower test matrix complexity | Requires migration updates in MySQL-specific tests |
| Schema governance | Single SQL dialect target for production | Initial refactor effort to remove multi-engine assumptions |
| Team focus | Clearer DBA and SRE playbooks | Loss of optionality for teams that prefer MySQL |

### Risks and mitigations

| Risk | Probability | Impact | Mitigation |
|---|---|---|---|
| Hidden MySQL-only assumptions break migration | Medium | High | Run side-by-side SQL verification and migration rehearsals |
| CI false confidence if H2 diverges from Postgres behavior | Medium | Medium | Increase Postgres-based integration coverage in pipeline |
| Team resistance due to legacy scripts | Medium | Medium | Time-boxed migration plan and documented fallback procedures |
| Migration delays due to environment parity issues | Medium | Medium | Standardized Postgres docker profile for local dev |

### Cost estimate

| Cost component | Estimate |
|---|---|
| One-time implementation | 8-14 engineer-weeks |
| Recurring operational delta | -USD 100 to +USD 250 per month (depends on environment consolidation) |
| Indirect cost | CI pipeline updates, team retraining, migration documentation |

### Expected outcomes

- Consistent persistence behavior from CI to production.
- Reduced release friction caused by multi-engine validation.
- Clearer ownership for schema evolution and incident response.
