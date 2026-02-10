# 🧭 Onboarding Log

**📘 Course Focus:** Requirements Engineering (AI‑Native) & Domain‑Driven Design  
**⏰ Deadline:** Week 2 – February 8th

**✍️ Authors:**
- Francisco Magdiel Asicona Mateo – 26006399
- Sergio Rolando Oliva del Valle – 26005694

---

## 🔍 Scope

Evaluate the project from download, setup, build, deploy, and verification.

## 🔁 Feedback Loops

- **⏱️ TTB (Time To Build):** ~5 minutes
- **🧪 Time for Testing:** Not clearly measurable; tests are not properly set up

## 🧠 Cognitive Load

- README exists but lacks sufficient setup details
- Documentation assumes:
    - A pre‑compiled version of the project
    - Correct Java version already installed
    - Prior knowledge on how to compile the project
- Tests exist but their correctness is uncertain

## 🌊 Flow State

- Manual compilation required
- Manual environment setup required
- Runtime exceptions appear while the application is running, unclear if expected

## 📊 Evidence

**⏰ Setup Time:** ~15–20 minutes

Steps and findings:
- ☕ Java 17 installation required (initial attempts were done with Java 21)
- 🔧 Manual compilation required
- ✅ Tests are not blocking the build (positive)
- ❌ Tests do not run during build (concerning)
- 📦 Git clone failed due to permissions; project was downloaded as ZIP

## ⚠️ Pain Points

- Digging into the correct JDK version
- Unclear setup instructions
- Poor technical and high‑level documentation
- Documentation mixes user manuals, class diagrams, and architecture

## 💡 Suggested Improvements

- Clearly document required JDK version
- Separate unit tests from functional tests
- Include unit tests in the build pipeline
- Document functional test setup separately
- Document required dependencies (Docker, MySQL, etc.)
- Refactor project structure to better classify tests

---

## 🚧 Identified Friction Points (DevEx Audit Summary)

Based on the onboarding experience and the documented setup process, the following friction points were identified. These represent concrete obstacles that negatively impact developer experience, onboarding speed, and confidence when working with the project.

### 1️⃣ Unclear Java Version Requirement

- The documentation does not explicitly state the required JDK version.
- Initial attempts to run the project with Java 21 failed, requiring trial-and-error to discover that Java 17 was needed.
- This increases setup time and cognitive load, especially for new contributors.

**Impact:** Slower onboarding, unnecessary context switching, and frustration during initial setup.

---

### 2️⃣ Incomplete and Assumptive Documentation

- The README assumes prior knowledge about how to compile and run the project.
- It implicitly assumes the user has a pre-compiled artifact or knows the full build process.
- Key steps (build commands, environment preparation) are not clearly documented.

**Impact:** New developers must reverse-engineer the setup instead of following a guided process.

---

### 3️⃣ Manual and Non-Automated Build Process

- The project requires manual compilation and environment configuration.
- There is no clear one-command build or standardized setup script.
- This breaks flow state and increases the likelihood of human error.

**Impact:** Reduced productivity and inconsistent local environments across developers.

---

### 4️⃣ Testing Setup Is Unclear and Not Integrated into the Build

- Tests exist, but it is unclear whether they are properly configured or reliable.
- Tests do not run as part of the normal build process.
- There is no guidance on how or when tests should be executed.

**Impact:** Low confidence in system stability and risk of undetected regressions.

---

### 5️⃣ Missing Documentation of External Dependencies

- Required dependencies such as databases, Docker, or other infrastructure components are not documented.
- Developers must infer or discover dependencies by inspecting the code.

**Impact:** Increased setup time and higher risk of misconfigured environments.
