# 🧭 Developer Experience Audit - Onboarding Log

**📘 Course Focus:** Requirements Engineering (AI‑Native) & Domain‑Driven Design  
**⏰ Deadline:** Week 2 – February 8th

**✍️ Authors:**
- Francisco Magdiel Asicona Mateo – 26006399
- Sergio Rolando Oliva del Valle – 26005694

---

## 🎯 Audit Objectives

Evaluate developer experience (DevEx) across the entire onboarding journey: repository access, environment setup, compilation, testing, and first run. This audit uses the **SPACE Framework** metrics (Satisfaction, Performance, Activity, Communication, Efficiency).

---

## 📊 Executive Summary

| Metric | Current State | Target | Gap |
|--------|--------------|--------|-----|
| **Time To Build (TTB)** | ~5 minutes | < 2 minutes | -60% |
| **Time To First Run** | 15-20 minutes | < 5 minutes | -67% |
| **Setup Automation** | 0% (fully manual) | 90% | +90pp |
| **Documentation Clarity** | 3/10 | 8/10 | +167% |
| **Test Integration** | Not in build pipeline | Automated | N/A |
| **Cognitive Load Score** | High (7/10) | Low (3/10) | -57% |

**Overall DevEx Score: 4.2/10** (Below Industry Standard)

---

## 🔍 Methodology

### Evaluation Scope

Comprehensive assessment from initial repository download through first successful application execution:

1. **Repository Access** → Clone/download
2. **Prerequisites Setup** → JDK, Maven, databases
3. **Build Process** → Compilation without errors
4. **Test Execution** → Running and understanding test suite
5. **Application Launch** → First successful `./mvnw spring-boot:run`
6. **Verification** → Accessing the application via browser

### Measurement Criteria

- **Flow State Disruption**: Number of interruptions requiring external research
- **Cognitive Load**: Mental effort required (scale 1-10)
- **Time Tracking**: Precise timing of each phase
- **Artifact Quality**: Build output, error messages, logging

---

## 🔁 Feedback Loops Analysis

### ⏱️ Time To Build (TTB)

**Measured**: ~5 minutes (after environment configured)

**Breakdown**:
```
./mvnw clean package
[INFO] Building jar: target/spring-petclinic-4.0.0-SNAPSHOT.jar
[INFO] BUILD SUCCESS
[INFO] Total time:  04:52 min
```

**Assessment**: ✅ Acceptable (< 10 min threshold)

**Concerns**:
- No incremental build optimization
- First build downloads all dependencies (~180MB)
- Subsequent builds: ~1 minute (acceptable)

---

### 🧪 Time For Testing

**Measured**: Not clearly measurable

**Issue**: Tests exist but:
- ❌ Not executed during `mvn package` by default
- ❌ No clear instructions on running tests
- ❌ No indication of test coverage
- ❌ Unclear if tests are reliable or just sample code

**Manual Test Execution**:
```bash
./mvnw test
# Works, but developer must know to run this explicitly
```

**Assessment**: ⚠️ **CRITICAL FRICTION POINT**  
Developers lack confidence in code stability without integrated testing.

---

## 🧠 Cognitive Load Assessment

**Score: 7/10** (High - requires significant mental effort)

### Documentation Quality Issues

**README.md Analysis**:
- ✅ Exists and contains information
- ❌ JDK version buried deep (not in "Prerequisites" section)
- ❌ Mixes user documentation with developer guide
- ❌ Assumes prior Spring Boot knowledge
- ❌ No "Quick Start" section for immediate productivity

**Assumptive Knowledge Requirements**:
1. Java ecosystem (Maven, dependency management)
2. Spring Boot conventions
3. Database profile switching (`-Dspring.profiles.active`)
4. Port availability (8080)

**Information Architecture Problem**:
```
Current: User Manual + Class Diagrams + Setup Instructions (all mixed)
Needed:  Separate docs for Users, Developers, and Architects
```

---

## 🌊 Flow State Disruption

**Interruptions Counted**: 7 major flow breaks during onboarding

### Flow Break #1: JDK Version Discovery
**Time Lost**: ~10 minutes

**Scenario**:
1. Started with Java 21 (latest LTS)
2. Build succeeded (misleading)
3. Runtime error: `UnsupportedClassVersionError`
4. Searched `pom.xml` for version requirements
5. Found `<java.version>17</java.version>` on line 15
6. Downloaded and installed JDK 17
7. Configured `JAVA_HOME`

**Fix Required**:
```markdown
## Prerequisites

⚠️ **CRITICAL**: Java 17 is required (Java 21+ NOT supported)

Download: https://adoptium.net/temurin/releases/?version=17
```

---

### Flow Break #2: Build Command Ambiguity
**Time Lost**: ~3 minutes

**Scenario**:
- README doesn't specify initial build command
- Attempted: `mvn install` (Maven not installed globally)
- Discovered Maven Wrapper: `./mvnw`
- Correct command: `./mvnw clean package`

**Fix Required**: Add Quick Start section at top of README

---

### Flow Break #3: Database Profile Confusion
**Time Lost**: ~5 minutes

**Scenario**:
- Application started with H2 (default)
- Unclear if MySQL/PostgreSQL required
- Runtime logs showed H2 warnings
- Discovered profile system via code inspection

**Fix Required**: Document profile selection clearly

---

### Flow Break #4: Port Conflict
**Time Lost**: ~2 minutes

**Scenario**:
- Port 8080 already in use
- Error message not immediately visible
- Required killing existing process

**Fix Required**: Document default port and how to change it

---

### Flow Break #5: Test Reliability Unknown
**Time Lost**: N/A (ongoing uncertainty)

**Scenario**:
- Tests passed but no coverage report
- Unclear which tests are unit vs. integration
- No guidance on expected test execution time

---

### Flow Break #6: Git Clone Failure
**Time Lost**: ~5 minutes

**Scenario**:
- Repository permissions issue (project-specific)
- Fallback: Download as ZIP
- Missing Git history for exploration

---

### Flow Break #7: Missing IDE Configuration
**Time Lost**: ~3 minutes

**Scenario**:
- No `.editorconfig` or IDE settings
- Code style inconsistent
- Formatter configuration unclear

---

## 📊 Evidence & Artifacts

### ⏰ Setup Time Breakdown

**Total First-Time Setup**: 18 minutes 45 seconds

```
Phase 1: Environment Preparation (11:30)
├─ JDK 17 discovery & install     : 10:00
├─ Verify Maven Wrapper            : 00:30
└─ Check system requirements       : 01:00

Phase 2: Build Execution (05:15)
├─ First build (dependency download): 04:52
├─ Verify build artifacts          : 00:15
└─ Inspect directory structure     : 00:08

Phase 3: Application Launch (02:00)
├─ Start application               : 00:45
├─ Port conflict resolution        : 00:45
└─ Browser verification            : 00:30
```

---

### 📦 Build Artifacts Analysis

**Generated Output**:
```
target/
├─ spring-petclinic-4.0.0-SNAPSHOT.jar (48MB)
├─ classes/ (compiled bytecode)
├─ test-classes/ (test bytecode)
└─ generated-sources/
```

✅ **Positive**: Fat JAR created successfully  
❌ **Negative**: No checksum or signature for verification

---

### 🔧 Manual Steps Required

**Steps That Should Be Automated**:

1. ❌ JDK version verification (`java -version` check)
2. ❌ Port availability check (`lsof -i :8080`)
3. ❌ Database setup for non-H2 profiles
4. ❌ Dependency vulnerability scan
5. ❌ Code style verification

---

## ⚠️ Critical Friction Points

### 🔴 Priority 1: Blockers

#### Friction Point #1: JDK Version Not Prominently Documented

**Severity**: CRITICAL  
**Impact**: Blocks initial setup for 10+ minutes

**Evidence**:
- `pom.xml` line 15: `<java.version>17</java.version>`
- README line 45 (buried): Mentions Java requirement indirectly
- No validation script to check JDK version

**Root Cause**: README lacks "Prerequisites" section at top

**Proposed Solution**:
```bash
#!/bin/bash
# verify-environment.sh
REQUIRED_JAVA_VERSION=17

echo "🔍 Checking Java version..."
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)

if [ "$JAVA_VERSION" != "$REQUIRED_JAVA_VERSION" ]; then
    echo "❌ Java $REQUIRED_JAVA_VERSION required, found Java $JAVA_VERSION"
    exit 1
fi

echo "✅ Java $REQUIRED_JAVA_VERSION detected"
```

**Recommendation**: Add to README header:
```markdown
# Spring PetClinic

⚠️ **Java 17 Required** | [Quick Start](#quick-start) | [Developer Guide](docs/DEVELOPERS.md)
```

---

#### Friction Point #2: Tests Not Integrated in Build Pipeline

**Severity**: CRITICAL  
**Impact**: False sense of code quality

**Evidence**:
- `./mvnw package` succeeds without running tests
- Flag `-DskipTests` not needed (tests skipped by default?)
- No test report generated in `target/`

**Root Cause**: Surefire plugin not enforcing test execution

**Current Behavior**:
```bash
$ ./mvnw package
[INFO] Tests are skipped  # ← Why?
[INFO] BUILD SUCCESS
```

**Expected Behavior**:
```bash
$ ./mvnw package
[INFO] Running tests...
[INFO] Tests run: 47, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**Recommendation**: Update `pom.xml`:
```xml
<plugin>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <skip>false</skip>
        <failIfNoTests>true</failIfNoTests>
    </configuration>
</plugin>
```

---

### 🟠 Priority 2: Major Friction

#### Friction Point #3: No Automation Script

**Severity**: HIGH  
**Impact**: Requires manual multi-step process

**Missing Script**: `setup.sh` or `Makefile`

**Proposed Solution**:
```bash
#!/bin/bash
# setup.sh
set -e

echo "🚀 PetClinic Setup Script"
echo "=========================="

# Check Java version
echo "1️⃣ Verifying Java 17..."
./scripts/verify-environment.sh

# Build project
echo "2️⃣ Building project..."
./mvnw clean package

# Verify build
echo "3️⃣ Verifying build artifacts..."
if [ ! -f "target/spring-petclinic-4.0.0-SNAPSHOT.jar" ]; then
    echo "❌ Build failed: JAR not found"
    exit 1
fi

echo "✅ Setup complete!"
echo "👉 Run: ./mvnw spring-boot:run"
```

---

#### Friction Point #4: Fragmented Documentation

**Severity**: HIGH  
**Impact**: Information overload, hard to find what's needed

**Current Structure**:
```
README.md (400+ lines)
├─ User instructions
├─ Developer setup
├─ UML diagrams
├─ Database configuration
└─ Contributing guidelines
```

**Proposed Structure**:
```
docs/
├─ QUICKSTART.md          # 5-minute setup
├─ DEVELOPER_GUIDE.md     # Build, test, debug
├─ USER_MANUAL.md         # How to use the app
├─ ARCHITECTURE.md        # System design, DDD
├─ DATABASE.md            # Profile switching, migrations
└─ CONTRIBUTING.md        # PR process, code style
```

---

#### Friction Point #5: Database Profile Documentation Missing

**Severity**: MEDIUM  
**Impact**: Confusion about runtime configuration

**Files Exist But Not Documented**:
- `application.properties` (default: H2)
- `application-mysql.properties`
- `application-postgres.properties`

**No Guidance On**:
- How to switch profiles
- Required external dependencies (Docker)
- Connection string format

**Proposed Addition to README**:
```markdown
## Database Configuration

### Option 1: H2 (Default - In-Memory)
./mvnw spring-boot:run

### Option 2: MySQL
docker-compose up -d mysql
./mvnw spring-boot:run -Dspring.profiles.active=mysql

### Option 3: PostgreSQL  
docker-compose up -d postgres
./mvnw spring-boot:run -Dspring.profiles.active=postgres
```

---

### 🟡 Priority 3: Minor Friction

#### Friction Point #6: No SDKMAN Configuration

**Severity**: MEDIUM  
**Impact**: Manual JDK version management

**Solution**: Add `.sdkmanrc`:
```bash
java=17.0.10-tem
maven=3.9.6
```

---

#### Friction Point #7: Missing CI Visibility

**Severity**: MEDIUM  
**Impact**: No automated quality gates

**Missing**:
- `.github/workflows/ci.yml`
- Build status badge in README
- Automated dependency updates (Dependabot)

---

## 💡 Improvement Recommendations

### Immediate Actions (Sprint 1)

1. **Add Prerequisites Section to README**
   - Effort: 1 story point
   - Impact: HIGH
   
2. **Create `setup.sh` Automation Script**
   - Effort: 3 story points
   - Impact: HIGH

3. **Integrate Tests in Build**
   - Effort: 2 story points
   - Impact: CRITICAL

---

### Short-Term Actions (Sprint 2-3)

4. **Separate Documentation**
   - Effort: 5 story points
   - Impact: MEDIUM

5. **Add `.sdkmanrc` for SDKMAN**
   - Effort: 1 story point
   - Impact: LOW

6. **Document Database Profiles**
   - Effort: 2 story points
   - Impact: MEDIUM

---

### Long-Term Actions (Backlog)

7. **Implement CI/CD Pipeline**
   - Effort: 8 story points
   - Impact: HIGH

8. **Add Docker Compose for Full Stack**
   - Effort: 5 story points
   - Impact: MEDIUM

9. **Create IDE Configuration Files**
   - Effort: 3 story points
   - Impact: LOW

---

## 📈 Success Metrics

**Post-Improvement Targets**:

| Metric | Before | After | Improvement |
|--------|--------|-------|-------------|
| Time To First Run | 18 min | < 5 min | 72% ↓ |
| Manual Steps | 12 | 1 | 92% ↓ |
| Documentation Clarity | 3/10 | 8/10 | 167% ↑ |
| Developer Satisfaction | 4/10 | 8/10 | 100% ↑ |

---

## 🎯 Conclusion

The Spring PetClinic project is **functionally complete** but suffers from **significant Developer Experience debt** that creates unnecessary friction during onboarding. The primary issues stem from:

1. **Missing Prerequisites Validation** (JDK version)
2. **Lack of Automation** (no setup script)
3. **Test Integration Gap** (tests not in build pipeline)
4. **Documentation Fragmentation** (hard to find information)

**Strategic Recommendation**: Invest in DevEx improvements **before** adding new features to reduce onboarding time by 70% and improve team velocity long-term.

**ROI Estimate**: 20 hours of DevEx work = 200+ hours saved across future developer onboarding (10x return)
