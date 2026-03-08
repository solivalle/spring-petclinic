# 🚀 Delivery 1: Discovery & Reverse Engineering

**📘 Course Focus:** Requirements Engineering (AI‑Native) & Domain‑Driven Design  
**⏰ Deadline:** Week 2 – February 8th

**✍️ Authors:**
- Francisco Magdiel Asicona Mateo – 26006399
- Sergio Rolando Oliva del Valle – 26005694

**File:** [`fase_1/fase1.md`](fase_1/fase1.md)

**File:** [`fase_3/snyk_overview.md`](fase_3/snyk_overview.md)

# 🏛️ Legacy Readme

**File:** [legacyreadme.md](docs/legacyreadme.md)

## Secret Protection (Husky pre-commit hook)

This repository uses `Husky` to run a `pre-commit` secret scan on staged changes. Commits are blocked when potential secrets are detected (API keys, tokens, private keys, etc.).

### One-time setup

```bash
npm install
```

`npm install` runs the `prepare` script (`husky`) and configures:

```bash
git config core.hooksPath .husky/_
```

### Validation

After setup, every `git commit` will run the secret scan automatically.

### Emergency bypass (not recommended)

```bash
SKIP_SECRET_SCAN=1 git commit -m "your message"
```
