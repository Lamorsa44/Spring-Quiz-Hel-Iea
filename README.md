Web Quiz Engine (Spring Boot)

Abstract
- A RESTful quiz engine where users can register, create quizzes, solve them, and view their completion history. The application exposes paginated endpoints, secures resources with HTTP Basic authentication, and persists data using Spring Data JPA with an H2 file database. It also records the quiz author automatically using JPA auditing.

Technologies Used
- Java 23 (Gradle toolchain)
- Spring Boot 3.3.x
  - Spring Web (REST API)
  - Spring Data JPA (persistence)
  - Spring Validation (bean validation)
  - Spring Security (HTTP Basic auth)
  - Spring Boot Actuator (management/shutdown)
- H2 Database (file-based)
- Gradle (build system)

Detailed Description

Overview
- This project is a simple Web Quiz Engine. Authenticated users can:
  - Create quizzes with multiple options and one or more correct answer indices.
  - Retrieve quizzes (single or paginated list) without exposing the correct answers.
  - Solve a quiz and receive instant feedback; correct solutions are recorded as completions.
  - View a paginated history of their completed quizzes, sorted from newest to oldest.
- New users can register with email and password; after registration, they can access the protected endpoints via HTTP Basic.

Architecture
- Entry point: src/engine/WebQuizEngine.java (Spring Boot application).
- Layers:
  - Controller (src/engine/business/Controller.java): Defines REST endpoints.
  - Service (src/engine/business/*.java): Business logic for quizzes and users.
  - Repository (src/engine/repository/*.java): Spring Data JPA repositories.
  - Model (src/engine/model/*.java): JPA entities (Quiz, MyUser, CompletedQuizz).
  - DTOs (src/engine/business/DTO/*.java): API request/response models.
  - Configuration (src/engine/configuration/*.java): Security, exception handling, Jackson, and JPA auditing.

Data Model (simplified)
- Quiz
  - id: Long (auto-generated)
  - title: String (not blank)
  - text: String (not blank)
  - options: List<String> (min size 2)
  - answer: List<Integer> (indices of the correct options)
  - author: String (set automatically from the authenticated user via JPA auditing)
- MyUser
  - id: Long
  - email: String (unique)
  - password: String (BCrypt-hashed)
- CompletedQuizz
  - id: Long
  - completedAt: LocalDateTime (set when a correct answer is submitted)
  - user: MyUser (who solved it)
  - quiz: Quiz (which quiz was solved)

Security
- HTTP Basic authentication.
- Public endpoints:
  - POST /api/register (user registration)
  - POST /actuator/shutdown (actuator shutdown)
  - /test/** (test-only endpoints)
- All other endpoints require authentication.
- Passwords are stored using BCrypt (see SecurityConfigMod.passwordEncoder()).
- JPA Auditing is enabled; the quiz author is recorded from the authenticated principal (email).

Validation Rules
- Registration (RegisterRequest):
  - email: must match pattern .+\.\w+ and be a valid email format
  - password: minimum length 5, not blank
- Quiz creation (QuizWithoutId):
  - title: not blank
  - text: not blank
  - options: at least 2
  - answer: list of integer indexes (if provided). Note: matching is order-sensitive based on current implementation.

Persistence & Configuration
- Database: H2 file database
  - JDBC URL: jdbc:h2:file:../quizdb
  - Username: sa
  - Password: password
  - Dialect: org.hibernate.dialect.H2Dialect
- JPA/Hibernate: ddl-auto=update
- H2 Console: enabled at /h2-console (secured unless you adjust security rules)
- Server port: 8889
- Actuator: all endpoints exposed; shutdown is enabled.
- See src/resources/application.properties for full configuration.

API Endpoints (core)
- Registration
  - POST /api/register (public)
    - Request body:
      {
        "email": "user@example.com",
        "password": "secret123"
      }
    - Response: 200 OK (empty body) or 400 on validation error; 400 if email already exists.

- Quizzes
  - GET /api/quizzes (auth required)
    - Query params: page, size, sort (Spring Pageable)
    - Response: Page of quizzes (QuizWithoutAnswer: id, title, text, options)
  - GET /api/quizzes/{id} (auth required)
    - Response: QuizWithoutAnswer
  - POST /api/quizzes (auth required)
    - Request body (QuizWithoutId):
      {
        "title": "The Java Question",
        "text": "Which are JVM languages?",
        "options": ["Java", "Kotlin", "Python", "Scala"],
        "answer": [0,1,3]
      }
    - Response: QuizWithoutAnswer (created quiz, without the answer field)
  - DELETE /api/quizzes/{id} (auth required)
    - Only the author can delete their quiz; otherwise 403 Forbidden.

- Solving
  - POST /api/quizzes/{id}/solve (auth required)
    - Request body (JustAnswer):
      {
        "answer": [0,1,3]
      }
    - Response (AnswerDTO):
      {
        "success": true,
        "feedback": "Congratulations, you're right!"
      }
    - On wrong answer: success=false with feedback message.
    - On correct answer, a CompletedQuizz entry is recorded.

- Completed quizzes
  - GET /api/quizzes/completed (auth required)
    - Query params: page, size, sort
    - Response: Page of CompletedQuizzDTO items: { id: <quizId>, completedAt: <ISO_LOCAL_DATE_TIME> }
    - Sorted by completedAt descending.

- Test endpoint
  - GET /test/users (public)
    - Returns a list of registered users (intended for testing).

Error Handling
- Centralized via ExceptionResolverMod with appropriate HTTP statuses:
  - 400 Bad Request for validation errors or domain validation (e.g., duplicate quiz title/quiz duplication, email already in use)
  - 401 Unauthorized when credentials are missing/invalid
  - 403 Forbidden when attempting to delete a quiz not authored by the user
  - 404 Not Found when a quiz or user is missing

How to Run
- Prerequisites: none explicitly, Gradle will provision the Java toolchain (Java 23) if needed.
- Using Gradle Wrapper (Windows PowerShell/CMD):
  - Build: gradlew.bat build
  - Run: gradlew.bat bootRun
- Application URL: http://localhost:8889
- H2 Console: http://localhost:8889/h2-console
  - JDBC URL: jdbc:h2:file:../quizdb
  - Username: sa
  - Password: password

Authentication Example (curl)
- After registering a user, use HTTP Basic on protected endpoints. Examples:
  - Register:
    curl -X POST http://localhost:8889/api/register ^
      -H "Content-Type: application/json" ^
      -d "{\"email\":\"user@example.com\",\"password\":\"secret123\"}"
  - Create quiz:
    curl -X POST http://localhost:8889/api/quizzes ^
      -u user@example.com:secret123 ^
      -H "Content-Type: application/json" ^
      -d "{\"title\":\"Q1\",\"text\":\"Pick JVM languages\",\"options\":[\"Java\",\"Kotlin\",\"Python\",\"Scala\"],\"answer\":[0,1,3]}"
  - Solve quiz:
    curl -X POST http://localhost:8889/api/quizzes/1/solve ^
      -u user@example.com:secret123 ^
      -H "Content-Type: application/json" ^
      -d "{\"answer\":[0,1,3]}"

Notes & Caveats
- Answer matching is currently order-sensitive (the submitted list must equal the stored list), which may or may not be desirable depending on your quiz rules.
- Email validation requires a dot-suffix (pattern .+\.\w+), e.g., user@example.com.
- The shutdown endpoint is enabled and publicly accessible (POST /actuator/shutdown). Use with caution.
