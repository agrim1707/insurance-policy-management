# Insurance Policy Management System (Java Lab Mini Project)

A backend-only REST API built with **Java 17 + Spring Boot 3** (Spring Web,
Spring Data JPA, Spring Security, Bean Validation, H2 in-memory database).
No UI/front-end — all endpoints are meant to be tested with cURL, Postman or
any HTTP client.

## Features

| Who | What they can do |
|---|---|
| Guest | Browse the list of available policy plans (by category too) |
| User (customer) | Register, purchase a policy, view purchased policies |
| User (customer) | Submit a **claim** with required details + supporting documents, track its status |
| Admin | Review pending claims — **approve** or **reject** them with remarks |

Spring features used: `@SpringBootApplication`, `@Configuration`, `@Bean`,
`@RestController`, `@Service`, `@Transactional`, `@Entity`, `@Repository`
(JpaRepository), `@RestControllerAdvice` + `@ExceptionHandler` for global error
handling, Bean Validation (`@Valid`, `@NotNull`, `@Email`, `@Size`, `@NotEmpty`…),
Java `record` DTOs, constructor dependency injection, and the `ApplicationRunner`
interface for seeding the admin account.

## How to run

### IntelliJ IDEA
1. Open the project folder (`pom.xml` gets auto-imported; wait for Maven to download).
2. Run **`InsurancePolicyManagementApplication`** (green arrow next to `main`).

### Any terminal (VS Code / cmd / PowerShell / Git Bash)
No Maven installation needed — the project ships with the Maven wrapper:

```bash
./mvnw spring-boot:run          # Linux / macOS / Git Bash
mvnw.cmd spring-boot:run        # Windows cmd/PowerShell
```

App starts at **http://localhost:8080**.
The database resets on every restart (in-memory H2 — perfect for a lab demo).

## Login credentials

| Username | Password | Role |
|---|---|---|
| `admin` | `Admin@123` | ADMIN |
| `john` (demo customer) | `John@123` | USER |

All endpoints (except registration and browsing) use **HTTP Basic auth**.
`ROLE_USER` users may purchase policies and raise claims; `ROLE_ADMIN` is
required for `/api/admin/**`.

## API reference

### Public
| Method | Path | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Create a customer account |
| GET | `/api/policies` | List plans (optional `?category=HEALTH`) |
| GET | `/api/policies/{id}` | Plan details |
| GET | `/my/claims` | (doc placeholder) |

### User (Basic auth of a USER account)
| Method | Path | Purpose |
|---|---|---|
| GET | `/api/me` | Current profile |
| POST | `/api/policies/{id}/purchase` | Buy a plan |
| GET | `/api/me/policies` | My purchased policies |
| POST | `/api/claims` | Submit claim |
| GET | `/api/me/claims` | My claims & statuses |

### Admin (Basic auth of ADMIN account)
| Method | Path | Purpose |
|---|---|---|
| GET | `/api/admin/claims` | List claims (`?status=PENDING`) |
| PUT | `/api/admin/claims/{id}/approve` | Approve |
| PUT | `/api/admin/claims/{id}/reject` | Reject (remarks compulsory) |

### Claims request body (POST /api/claims)
```json
{
  "purchasedPolicyId": 1,
  "claimAmount": 25000.00,
  "incidentDescription": "Suffered a road accident on 28th Oct evening; hospitalised for two days.",
  "documents": ["hospital_bill.pdf", "police_report.pdf"]
}
```

## Quick test with cURL (Git Bash / Linux)

```bash
BASE=http://localhost:8080

# 1. register a new user
curl -s -X POST $BASE/api/auth/register -H "Content-Type: application/json" \
  -d '{"fullName":"Ravi Kumar","username":"ravi","email":"ravi@mail.com","password":"Pass@123"}'

# 2. browse policies (no login needed)
curl -s $BASE/api/policies
curl -s "$BASE/api/policies?category=HEALTH" | jq '.[0]'

# 3. purchase as john
curl -s -u john:John@123 -X POST $BASE/api/policies/3/purchase

# 4. my purchased policy (note its id for the claim)
curl -s -u john:John@123 $BASE/api/me/policies

# 5. submit a claim with documents
curl -s -u john:John@123 -X POST $BASE/api/claims -H "Content-Type: application/json" \
  -d '{"purchasedPolicyId":1,"claimAmount":50000,"incidentDescription":"Hospitalised after accident on 28 Oct evening.","documents":["hospital_bill.pdf","police_report.pdf"]}'

# 6. ADMIN reviews pending claims
curl -s -u admin:Admin@123 "$BASE/api/admin/claims?status=PENDING"

# 7. approve / reject
curl -s -u admin:Admin@123 -X PUT $BASE/api/admin/claims/1/approve \
  -H "Content-Type: application/json" -d '{"remarks":"Documents verified. Approved."}'
curl -s -u admin:Admin@123 -X PUT $BASE/api/admin/claims/1/reject \
  -H "Content-Type: application/json" -d '{"remarks":"Claim amount exceeds coverage."}'
```

## Class overview (`src/main/java/com/lab/insurance`)

```
InsurancePolicyManagementApplication.java   main class
model/          Policy, User, UserPolicy, Claim + enums (Role, ClaimStatus, PolicyCategory)
repository/     Spring Data JPA interfaces
service/        UserService, PolicyService, ClaimService (business logic)
controller/     UserController, PolicyController, PolicyMeController,
                ClaimController, AdminController (REST endpoints)
dto/            RegisterRequest, ClaimRequest, ClaimDecisionRequest, MessageResponse (Java records)
security/       SecurityConfig (Spring Security setup), CustomUserDetailsService, AdminSeed
exception/      GlobalExceptionHandler, ResourceNotFoundException, InvalidActionException
```

## H2 database console

While the app runs: open `http://localhost:8080/h2-console`
(JDBC URL `jdbc:h2:mem:insurance_db`, user `sa`, empty password) to inspect the
tables `users`, `policies`, `user_policies`, `claims`, `claim_documents`.
