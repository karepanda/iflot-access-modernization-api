Conversation opened. 1 unread message.

Skip to content
Using Gmail with screen readers

3 of 509
README Iflow access API
Inbox

Kenny Perez
Attachments
Sat, Mar 21, 1:34 PM (2 days ago)
to me

Debes actualizar cuando creen el repo es la URL del git clone.
 One attachment
  •  Scanned by Gmail
# iFlot Access API

Backend service responsible for modernizing access control in the iFlot platform.

---

## Overview

iFlot is a transportation management system originally built as a legacy client-server application.

This project extracts and rebuilds the access control module as a modern REST API, providing a centralized and secure foundation for authentication, authorization, and user administration.

For full business context and project goals, see the [Project Brief](docs/iflot-access-modernization-project-brief.md).

---

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Security | Spring Security + JWT |
| Persistence | Spring Data JPA + PostgreSQL |
| Documentation | SpringDoc OpenAPI (Swagger UI) |
| Build tool | Maven |
| Local runtime | Docker + Docker Compose |

---

## Architecture

This project uses a **3-layer architecture**.

```text
HTTP Request
     │
     ▼
┌─────────────────────────────┐
│     Presentation layer      │  Controllers — receive requests, return responses
└─────────────┬───────────────┘
              │
              ▼
┌─────────────────────────────┐
│       Service layer         │  Services — business logic and use case orchestration
└─────────────┬───────────────┘
              │
              ▼
┌─────────────────────────────┐
│     Data access layer       │  Repositories — read and write to the database
└─────────────────────────────┘
```

The dependency direction is always top-down: **Controller → Service → Repository**

For a detailed explanation of responsibilities, rules, and what to avoid in each layer, read the [Architecture Guide](docs/architecture-guide-and-rules.md).

### Package structure

```
com.iflot.access
├── config          # Spring configuration and OpenAPI setup
├── controller      # REST controllers and HTTP entry points
├── dto             # Request and response models (API contracts)
├── entity          # JPA entities (persistence models)
├── exception       # Custom exceptions and centralized error handling
├── repository      # Spring Data repositories
├── security        # JWT utilities, filters, and security configuration
└── service         # Application logic and use case orchestration
```

---

## Prerequisites

- [Java 21](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

---

## Local setup

### 1. Clone the repository

```bash
git clone https://github.com/your-org/iflot-access-api.git
cd iflot-access-api
```

### 2. Start the database

```bash
docker compose up -d
```

This starts a PostgreSQL container on port `5432`. Verify it is running:

```bash
docker compose ps
```

### 3. Configure environment variables

```bash
cp .env.example .env
```

The default values work for local development out of the box. Key variables:

| Variable | Default | Description |
|---|---|---|
| `DB_HOST` | `localhost` | PostgreSQL host |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_NAME` | `iflot_access` | Database name |
| `DB_USERNAME` | `iflot` | Database user |
| `DB_PASSWORD` | `iflot123` | Database password |
| `JWT_SECRET` | *(see .env.example)* | Secret key for JWT signing |
| `JWT_EXPIRATION_MS` | `86400000` | Token expiration in ms (24h) |

### 4. Build and run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.

---

## Default seed data

On first startup, the application creates the following data automatically:

| Data | Value |
|---|---|
| Default roles | `ADMIN`, `USER` |
| Admin username | `admin` |
| Admin email | `admin@iflot.com` |
| Admin password | `Admin1234!` |

> These credentials are for local development only. Passwords are always stored encrypted.

---

## API documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI specification in JSON:

```
http://localhost:8080/v3/api-docs
```

---

## Authentication

```
POST /auth/login
```

```json
{
  "username": "admin",
  "password": "Admin1234!"
}
```

For protected endpoints, include the token in every request:

```
Authorization: Bearer <token>
```

---

## Working conventions

### Backlog

The project backlog is managed in **GitHub Projects**.
Stories, priorities, and current status are tracked there.

User story definitions are available in [`docs/stories/`](docs/stories/).

### Git workflow

This project uses **Gitflow**.

| Branch | Purpose |
|---|---|
| `main` | Stable, releasable code |
| `develop` | Integration branch for completed stories |
| `feature/hu*` | One branch per story, e.g. `feature/hu5-docker` |

Rules:
- never commit directly to `main` or `develop`
- open a pull request from your feature branch into `develop`
- write clear commit messages that explain what was done and why

### Definition of Done

A story is considered done when:

- the implementation matches the acceptance criteria
- the application compiles and starts without errors
- local testing has been completed
- the API documentation reflects the new endpoints
- changes have been committed and pushed to the feature branch
- the pull request has been opened and reviewed

---

## Repository structure

```
iflot-access-api/
├── docs/
│   ├── architecture-guide-and-rules.md
│   ├── archunit-enforcement-plan.md
│   ├── iflot-access-modernization-project-brief.md
│   └── stories/
│       ├── HU3.md
│       ├── HU4.md
│       └── ...
├── src/
│   └── main/
│       └── java/
│           └── com/iflot/access/
├── docker-compose.yml
├── .env.example
├── pom.xml
└── README.md
```

---

## Related documentation

- [Architecture Guide and Rules](docs/architecture-guide-and-rules.md)
- [ArchUnit Enforcement Plan](docs/archunit-enforcement-plan.md)
- [Project Brief](docs/iflot-access-modernization-project-brief.md)
- [Stories](docs/stories/)
README.md
Displaying README.md.
