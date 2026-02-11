# 📝 Backlog Recovery - Reverse-Engineered User Stories

**📘 Course Focus:** Requirements Engineering (AI‑Native) & Domain‑Driven Design  
**⏰ Deadline:** Week 2 – February 8th

**✍️ Authors:**
- Francisco Magdiel Asicona Mateo – 26006399
- Sergio Rolando Oliva del Valle – 26005694

---

## 🎯 Methodology

User stories were extracted exclusively from business logic found in controllers, services, and domain models. Each story includes:
- **Traceability**: Direct link to source file and method
- **Acceptance Criteria**: Derived from validation logic in code
- **Priority**: Based on controller complexity and feature usage

---

## 🐶 Owner Management Context

### Owner CRUD Operations

**US-001: Create Owner**  
**As a** receptionist,  
**I want to** register a new owner with address, city, and telephone,  
**so that** I can maintain a complete customer registry.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/owner/OwnerController.java`  
🔧 **Methods**: `initCreationForm()` (L72), `processCreationForm()` (L78)  
✅ **Acceptance Criteria**:
- Address is required (`@NotBlank`)
- City is required (`@NotBlank`)
- Telephone must match pattern `\d{10}` (exactly 10 digits)
- Flash message "New Owner Created" on success
- Redirect to owner details page after creation

---

**US-002: Search Owner by Last Name with Pagination**  
**As a** receptionist,  
**I want to** search owners by last name using pagination (5 results per page),  
**so that** I can quickly find customer records even with large datasets.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/owner/OwnerController.java`  
🔧 **Methods**: `processFindForm()` (L99), `findPaginatedForOwnersLastName()` (L127)  
✅ **Acceptance Criteria**:
- Search supports partial matching (starts with)
- Empty search returns all owners
- Results limited to 5 per page
- Single result redirects directly to details
- No results shows "not found" error
- Pagination controls display page numbers

---

**US-003: View Owner Details with Pet History**  
**As a** receptionist,  
**I want to** view complete owner details including pets and medical visits,  
**so that** I have full visibility of the customer relationship.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/owner/OwnerController.java`  
🔧 **Methods**: `findOwner()` (L64) via `@ModelAttribute`  
✅ **Acceptance Criteria**:
- Display owner: firstName, lastName, address, city, telephone
- List all associated pets eagerly loaded (`FetchType.EAGER`)
- Show pet visits in chronological order
- Throw exception if owner ID not found

---

**US-004: Update Owner Information**  
**As a** receptionist,  
**I want to** edit owner details,  
**so that** I can maintain up-to-date customer information.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/owner/OwnerController.java`  
🔧 **Methods**: `initUpdateOwnerForm()` (L143), `processUpdateOwnerForm()` (L148)  
✅ **Acceptance Criteria**:
- Pre-fill form with existing data
- Validate ID mismatch between URL and form
- Apply same validation rules as creation
- Flash error message on validation failure
- Redirect to owner details after successful update

---

**US-005: Prevent ID Tampering in Updates**  
**As a** system,  
**I want to** validate that form ID matches URL ID,  
**so that** unauthorized modifications are blocked.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/owner/OwnerController.java`  
🔧 **Logic**: `Objects.equals(owner.getId(), ownerId)` (L154)  
✅ **Acceptance Criteria**:
- Reject request if IDs don't match
- Error message: "The owner ID in the form does not match the URL"
- ID field disabled in form data binding (`setDisallowedFields("id")`)

---

### Pet Management

**US-006: Register Pet for Owner**  
**As a** receptionist,  
**I want to** register a pet indicating type and birth date,  
**so that** the pet is properly linked to its owner.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/owner/PetController.java`  
🔧 **Methods**: `initCreationForm()` (L100), `processCreationForm()` (L107)  
✅ **Acceptance Criteria**:
- Pet type selected from predefined list (dropdown)
- Birth date in format `yyyy-MM-dd`
- Pet automatically associated with owner via `owner.addPet(pet)`
- Cascade save persists pet with owner
- Flash message: "New Pet has been Added"

---

**US-007: Validate Unique Pet Name per Owner**  
**As a** system,  
**I want to** ensure pet names are unique per owner,  
**so that** there's no confusion between pets.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/owner/PetController.java`  
🔧 **Logic**: `owner.getPet(pet.getName(), true) != null` (L114)  
✅ **Acceptance Criteria**:
- Check only for new pets (ignoring unsaved pets)
- Error: "already exists" if duplicate found
- Duplicate check case-insensitive
- Allow same name for different owners

---

**US-008: Validate Pet Birth Date**  
**As a** system,  
**I want to** reject future birth dates,  
**so that** data integrity is maintained.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/owner/PetController.java`  
🔧 **Logic**: `pet.getBirthDate().isAfter(LocalDate.now())` (L118)  
✅ **Acceptance Criteria**:
- Compare against current system date
- Field error: `typeMismatch.birthDate`
- Display error message on form
- Allow today's date as valid

---

**US-009: Update Pet Information**  
**As a** receptionist,  
**I want to** edit pet details,  
**so that** I can correct or update pet records.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/owner/PetController.java`  
🔧 **Methods**: `initUpdateForm()` (L136), `processUpdateForm()` (L141)  
✅ **Acceptance Criteria**:
- Pre-fill form with existing pet data
- Validate name uniqueness excluding current pet
- Validate birth date not in future
- Flash message: "Pet details has been edited"
- Redirect to owner details page

---

**US-010: Select Pet Type from Catalog**  
**As a** receptionist,  
**I want to** choose pet type from a predefined list,  
**so that** pets are properly categorized.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/owner/PetController.java`  
🔧 **Methods**: `populatePetTypes()` (L61) via `@ModelAttribute`  
🔧 **Repository**: `PetTypeRepository.findPetTypes()`  
✅ **Acceptance Criteria**:
- Load types from database (cat, dog, lizard, snake, bird, hamster)
- Display as dropdown in form
- Required field validation
- Stored as many-to-one relationship in Pet entity

---

### Visit Management

**US-011: Record Medical Visit**  
**As a** receptionist,  
**I want to** register a medical visit with date and description,  
**so that** the pet's medical history is complete.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/owner/VisitController.java`  
🔧 **Methods**: `initNewVisitForm()` (L89), `processNewVisitForm()` (L95)  
✅ **Acceptance Criteria**:
- Date defaults to today (`LocalDate.now()` in Visit constructor)
- Description is required (`@NotBlank`)
- Visit automatically linked to specific pet
- Owner must exist (validated via `loadPetWithVisit`)
- Flash message: "Your visit has been booked"

---

**US-012: View Pet Visit History**  
**As a** veterinarian,  
**I want to** view all visits for a pet in chronological order,  
**so that** I can understand previous treatments.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/owner/Pet.java`  
🔧 **Relationship**: `@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)`  
🔧 **Ordering**: `@OrderBy("date ASC")` (L57)  
✅ **Acceptance Criteria**:
- Visits loaded eagerly with pet
- Sorted by visit date ascending (oldest first)
- Display date and description for each visit
- Accessible via `pet.getVisits()`

---

**US-013: Auto-assign Current Date to Visit**  
**As a** system,  
**I want to** automatically set visit date to today,  
**so that** data entry is simplified.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/owner/Visit.java`  
🔧 **Constructor**: `this.date = LocalDate.now()` (L52)  
✅ **Acceptance Criteria**:
- Default constructor sets current date
- Date can be overridden if needed
- Uses system timezone
- Formatted as `yyyy-MM-dd` in forms

---

## 🩺 Vet Management Context

**US-014: List Veterinarians with Pagination**  
**As an** administrator,  
**I want to** view veterinarians with their specialties using pagination,  
**so that** I can manage medical staff efficiently.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/vet/VetController.java`  
🔧 **Methods**: `showVetList()` (L43), `findPaginated()` (L64)  
✅ **Acceptance Criteria**:
- Display 5 vets per page
- Show firstName, lastName, and specialties
- Pagination controls (current page, total pages, total items)
- Specialties sorted alphabetically
- Endpoint: `/vets.html`

---

**US-015: REST API for Veterinarians**  
**As an** external system,  
**I want to** consume vet data via REST API,  
**so that** I can integrate with other applications.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/vet/VetController.java`  
🔧 **Methods**: `showResourcesVetList()` (L70) with `@ResponseBody`  
✅ **Acceptance Criteria**:
- Endpoint: `GET /vets`
- Returns all vets (no pagination)
- Support JSON and XML via content negotiation
- Includes specialties in response
- Uses `Vets` wrapper class for XML serialization

---

**US-016: Manage Vet Specialties (Many-to-Many)**  
**As an** administrator,  
**I want to** assign multiple specialties to a veterinarian,  
**so that** their expertise is accurately represented.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/vet/Vet.java`  
🔧 **Relationship**: `@ManyToMany(fetch = FetchType.EAGER)` (L47)  
🔧 **Join Table**: `vet_specialties` with `vet_id` and `specialty_id`  
✅ **Acceptance Criteria**:
- Vet can have zero or many specialties
- Specialty can be assigned to multiple vets
- Specialties displayed sorted by name
- Method `addSpecialty(Specialty)` for association
- Count available via `getNrOfSpecialties()`

---

## 🖥️ System / Infrastructure Context

**US-017: Welcome Page**  
**As a** system user,  
**I want to** see a welcome screen at application root,  
**so that** I understand the purpose of the system.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/system/WelcomeController.java`  
🔧 **Route**: `@GetMapping("/")` → template `welcome.html`  
✅ **Acceptance Criteria**:
- Displayed when accessing root URL
- Contains application description
- Links to main features (owners, vets)

---

**US-018: Cache Veterinarian Data**  
**As a** system administrator,  
**I want** frequently accessed vet data cached using JCache,  
**so that** database load is reduced and performance improved.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/system/CacheConfiguration.java`  
🔧 **Configuration**: `createCache("vets", cacheConfiguration())` (L38)  
🔧 **Provider**: Caffeine (high-performance Java cache)  
✅ **Acceptance Criteria**:
- Cache named "vets" created at startup
- Statistics enabled for monitoring
- Applied to read-heavy operations
- Eviction strategy configurable

---

**US-019: Error Simulation Endpoint**  
**As a** developer,  
**I want to** trigger runtime exceptions via `/oups`,  
**so that** I can test error handling.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/system/CrashController.java`  
🔧 **Route**: `@GetMapping("/oups")` → throws `RuntimeException`  
✅ **Acceptance Criteria**:
- Endpoint accessible when testing
- Exception shows custom error template
- Error page displays application message

---

**US-020: Standardize Date Format**  
**As a** system,  
**I want to** use ISO date format `yyyy-MM-dd` consistently,  
**so that** date input/output is standardized.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/system/WebConfiguration.java`  
🔧 **Configuration**: Custom `DateTimeFormatter` registration  
✅ **Acceptance Criteria**:
- Applied to all LocalDate fields
- Used in forms via `@DateTimeFormat` annotation
- Validation rejects non-conforming dates

---

## 🔗 Shared Kernel (Technical Stories)

**US-021: Common Identity Strategy**  
**As a** developer,  
**I want to** use `BaseEntity` with auto-generated IDs,  
**so that** persistence is standardized.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java`  
🔧 **Strategy**: `@GeneratedValue(strategy = GenerationType.IDENTITY)` (L35)  
✅ **Acceptance Criteria**:
- All entities extend BaseEntity
- ID assigned by database (auto-increment)
- `isNew()` method checks if entity persisted
- Implements Serializable

---

**US-022: Person Abstraction**  
**As a** developer,  
**I want to** reuse `Person` abstract class for human entities,  
**so that** firstName/lastName logic isn't duplicated.

📂 **File**: `src/main/java/org/springframework/samples/petclinic/model/Person.java`  
✅ **Acceptance Criteria**:
- Used by Owner and Vet entities
- Validation: both fields required (`@NotBlank`)
- Extends BaseEntity (inherits ID management)

---

**US-023: Jakarta Bean Validation**  
**As a** system,  
**I want to** use declarative validation annotations,  
**so that** input is validated before persistence.

📂 **File**: Multiple entities (Owner, Visit, etc.)  
🔧 **Annotations**: `@NotBlank`, `@Pattern(regexp = "\\d{10}")`, `@DateTimeFormat`  
✅ **Acceptance Criteria**:
- Validation triggered automatically in controllers
- Errors bound to `BindingResult`
- Display field-level errors in forms
- Prevent invalid data from reaching database

---

## 📊 Story Metrics

| Context | User Stories | Priority | Complexity |
|---------|-------------|----------|------------|
| Owner Management | 13 | High | Medium-High |
| Vet Management | 3 | Medium | Low-Medium |
| System Context | 4 | Low | Low |
| Shared Kernel | 3 | Medium | Low |
| **TOTAL** | **23** | - | - |

---

## 🔍 Traceability Matrix

All user stories are traceable to:
1. **Source file** (exact Java class)
2. **Method/line numbers** (specific implementation)
3. **Acceptance criteria** (derived from code validation)

This ensures requirements are grounded in actual system behavior, not assumptions.
