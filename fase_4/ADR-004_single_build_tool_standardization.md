# ADR-004 - Single Build Tool Standardization

## Status

Proposed

## Date

2026-03-21

## Context

### Current build evidence

The repository currently keeps two first-class build definitions:

| Evidence | Repository source | Observation |
|---|---|---|
| Maven build definition | `pom.xml` | Full dependency and plugin graph for packaging/testing. |
| Gradle build definition | `build.gradle` and `settings.gradle` | Parallel dependency and plugin graph is also maintained. |
| Wrapper scripts for both tools | `mvnw`, `gradlew` | CI/local teams can use different build paths by default. |
| Potential version divergence | `pom.xml` vs `build.gradle` | Boot version and plugin versions may drift over time. |

### Problem

Dual build systems increase maintenance and governance cost:

- duplicated dependency/plugin management,
- higher CI matrix complexity,
- increased onboarding and troubleshooting variance.

### Architectural drivers

- Establish one paved-road pipeline for reproducibility.
- Reduce configuration drift risk.
- Simplify release and quality gates.

## Decision

Standardize on a single build tool for CI and release pipelines.

### Decision details

- Define one source of truth for dependencies/plugins and build lifecycle.
- Keep secondary build files only during a short transition window.
- Require all quality gates (test, style, packaging) to run through selected tool.
- Publish migration playbook for local developer workflows.

### Alternatives considered

1. Keep Maven and Gradle as equal long-term options.
2. Standardize on one build tool (selected).
3. Replace both with custom scripted build orchestration.

Option 2 is selected due lowest long-term operational complexity.

## Consequences

### Trade-offs

| Area | Positive consequence | Negative consequence |
|---|---|---|
| CI/CD | Single reproducible build path | Migration effort for users of non-selected tool |
| Governance | Less dependency/plugin drift | Temporary transition overhead |
| Onboarding | Clear default workflow | Team preference trade-offs |
| Tooling | Easier docs and support | Some local scripts need updates |

### Risks and mitigations

| Risk | Probability | Impact | Mitigation |
|---|---|---|---|
| Team friction due to tool preference | Medium | Medium | Time-boxed transition and helper scripts |
| Hidden pipeline dependencies on secondary tool | Medium | High | Pre-migration CI audit and dry-runs |
| Build regressions during cutover | Medium | High | Parallel validation window before decommission |

### Cost estimate

| Cost component | Estimate |
|---|---|
| One-time implementation | 3-6 engineer-weeks |
| Recurring operational delta | Net reduction (less duplicated maintenance effort) |
| Indirect cost | Team enablement and docs refresh |

### Expected outcomes

- Faster incident triage for build failures.
- Lower maintenance burden on release engineering.
- Clearer onboarding path for new contributors.
