# Architecture Guide and Rules

## Purpose

This document explains the architecture selected for the **iFlot Access API**.

Its goal is not only to define rules.
Its goal is also to help junior developers understand:

- what a layered architecture is
- why this project uses it
- what each layer is responsible for
- how data flows through the application
- what kinds of mistakes this structure helps prevent

This document should be read before implementing new features.

---

## Why architecture matters

Architecture is not just about folders or package names.

Architecture is about:
- separating responsibilities
- reducing accidental complexity
- making the system easier to understand
- making the code easier to change
- preventing chaos as the project grows

A project can work at the beginning even with poor structure.
The problem appears later, when:
- controllers become too large
- business logic gets duplicated
- persistence details leak everywhere
- no one knows where new code should go

A good architecture gives the team a shared mental model.

---

## Selected architecture style

This project uses a **3-layer architecture**.

The three layers are:

- **Presentation layer**
- **Service layer**
- **Data access layer**

This is a good fit for the current scope because the project has:
- one backend application
- one REST API
- one relational database
- simple to moderate business logic
- no immediate need for multiple interchangeable adapters

The goal is to keep the architecture:
- clear
- practical
- maintainable
- appropriate for the current level of complexity

This project does **not** need a more complex architecture at this stage.

---

## What a 3-layer architecture means

A layered architecture organizes code by responsibility.

Each layer has a clear purpose.

### Presentation layer
This is the entry point of the application.

It is responsible for:
- receiving HTTP requests
- validating basic input
- calling the service layer
- returning HTTP responses

It should not make business decisions.

### Service layer
This is the application logic layer.

It is responsible for:
- implementing use cases
- coordinating workflow
- applying business rules
- interacting with repositories
- deciding what should happen in the application

This is where the main behavior of the system lives.

### Data access layer
This layer is responsible for persistence.

It is responsible for:
- reading from the database
- writing to the database
- querying entities

It should not decide business behavior.

---

## Why this separation helps

This separation improves the codebase in several ways.

### 1. It makes the code easier to read
A developer can quickly understand where to look:
- HTTP concerns in controllers
- application logic in services
- persistence in repositories

### 2. It reduces coupling
If every class does everything, changes become risky.
Layers help reduce unnecessary dependencies.

### 3. It improves testability
Each layer can be tested with a different purpose:
- controllers for API behavior
- services for business flow
- repositories for persistence behavior

### 4. It supports maintainability
As the project grows, clear boundaries reduce confusion.

### 5. It creates discipline
A team with a shared structure makes more consistent decisions.

---

## Typical request flow

A normal request should follow this flow:

1. A client sends an HTTP request
2. A controller receives it
3. The controller validates basic input
4. The controller calls a service
5. The service applies the use case logic
6. The service reads or writes data through repositories
7. The result is returned to the controller
8. The controller returns an HTTP response

This means the main direction is:

**Controller -> Service -> Repository**

Not the other way around.

---

## Example

### Good example
A user sends a login request.

- The controller receives the request
- The service validates credentials and builds the authentication flow
- The repository loads the user from the database
- The service decides whether authentication succeeds
- The controller returns the response

### Bad example
A controller directly queries the repository, validates passwords, creates tokens, and builds custom error logic.

That may work once.
But it mixes too many concerns in the same place.

---

## Package structure

The base package should follow this structure:

- `com.iflot.access`
  - `config`
  - `controller`
  - `dto`
  - `entity`
  - `exception`
  - `repository`
  - `security`
  - `service`

This structure makes the architecture visible in the codebase.

---

## Package responsibilities

## `controller`
Contains REST controllers and HTTP entry points.

Use this package for:
- request handling
- response building
- endpoint definitions
- request validation annotations

Do not use this package for:
- business rules
- repository access
- persistence logic

---

## `service`
Contains application logic and use case orchestration.

Use this package for:
- business flow
- coordination between components
- authorization decisions when part of application logic
- validations that belong to the use case

Do not use this package for:
- raw HTTP concerns
- endpoint mapping
- direct API contract definitions

---

## `repository`
Contains persistence access.

Use this package for:
- Spring Data repositories
- database queries
- entity retrieval and persistence

Do not use this package for:
- business logic
- HTTP behavior
- response construction

---

## `entity`
Contains JPA entities.

Use this package for:
- persistence models
- relational mappings
- database-oriented structures

Entities represent stored data.
They do not represent public API contracts.

---

## `dto`
Contains API request and response models.

Use this package for:
- request bodies
- response bodies
- public API contracts

DTOs should stay simple.
They should not contain business logic or persistence annotations.

---

## `security`
Contains security-related components.

Use this package for:
- JWT utilities
- authentication filters
- security configuration helpers
- authentication-related support classes

This keeps security concerns centralized.

---

## `config`
Contains Spring configuration classes.

Use this package for:
- bean configuration
- OpenAPI configuration
- application-level setup
- security configuration classes when appropriate

---

## `exception`
Contains error handling logic.

Use this package for:
- custom exceptions
- centralized exception handling
- consistent API error responses

This helps keep error handling predictable and uniform.

---

## Dependency direction

The intended dependency direction is:

- controllers may depend on services and DTOs
- services may depend on repositories, entities, DTOs, exceptions, and security helpers when needed
- repositories may depend on entities
- entities should remain persistence-focused

The following must be avoided:

- controllers depending directly on repositories
- services depending on controllers
- repositories depending on services
- cyclic dependencies between top-level packages

---

## What each layer should not do

Understanding what a layer should **not** do is just as important.

### Controllers should not
- implement business rules
- call repositories directly
- decide persistence behavior
- become large orchestration classes

### Services should not
- expose HTTP details
- return framework-specific web concerns unnecessarily
- become dumping grounds for unrelated logic

### Repositories should not
- contain application decisions
- coordinate workflows
- know about controllers or HTTP requests

### Entities should not
- be used directly as public API responses by default
- carry controller-specific behavior
- replace DTOs

---

## Common mistakes in layered applications

These are common problems in junior projects.

## 1. Fat controllers
The controller does too much:
- validation
- business logic
- persistence
- response handling

This makes controllers hard to maintain.

## 2. Anemic or empty services
The service layer becomes a pass-through with no real responsibility.

This usually means business logic is leaking into controllers or repositories.

## 3. Repositories with business logic
Repositories should retrieve or persist data.
They should not decide what the application should do.

## 4. Exposing entities directly
This couples the API contract to the persistence model.

It becomes harder to evolve the database safely.

## 5. Mixed responsibilities
A single class starts doing multiple jobs because “it was faster.”

That shortcut often becomes future confusion.

---

## How this relates to clean architecture thinking

This project is not using a full Clean Architecture or Hexagonal Architecture implementation.

However, it still benefits from the same core ideas:
- clear boundaries
- explicit responsibilities
- low coupling
- conscious design decisions
- separation between transport, logic, and persistence concerns

In other words:
this is a simpler architecture style,
but it should still be implemented with discipline.

---

## Core rules

## 1. Controllers only handle HTTP concerns
Controllers are responsible for:
- receiving HTTP requests
- validating basic input
- delegating to services
- returning HTTP responses

Controllers must not:
- implement business rules
- access repositories directly
- contain persistence logic

---

## 2. Services contain application logic
Services are responsible for:
- orchestrating use cases
- applying business rules
- calling repositories
- coordinating validation and workflow

Services must not:
- depend on controllers
- become HTTP-oriented classes

---

## 3. Repositories only handle persistence
Repositories are responsible for:
- reading data
- writing data
- querying the database

Repositories must not:
- contain business rules
- depend on controllers
- depend on services for workflow decisions

---

## 4. Entities must not leak through the public API
JPA entities should not be used directly as public request or response models.

Use DTOs for:
- API requests
- API responses
- external contracts

---

## 5. DTOs are API-facing models only
DTOs must:
- represent request and response data
- remain simple
- avoid business logic
- avoid persistence annotations

---

## 6. Security concerns must be centralized
Security-related code should live in `security` or `config`.

Do not scatter authentication behavior across unrelated packages.

---

## 7. No cyclic dependencies between packages
Top-level packages must not depend on each other in cycles.

Cycles are a sign that responsibilities are becoming unclear.

---

## 8. Package structure must express architecture
Classes should be placed in the package that matches their responsibility.

Avoid vague names such as:
- `misc`
- `helper`
- `manager`
- `stuff`

---

## 9. Exceptions must be handled consistently
Errors should be managed in a centralized and predictable way.

Prefer:
- custom exceptions when needed
- centralized exception handling
- consistent HTTP responses

---

## 10. Shortcuts must be conscious and documented
This is a portfolio project and some simplifications are acceptable.

Examples:
- using 3 layers instead of a more advanced architecture
- starting with roles before granular permissions
- keeping the first iteration small

Shortcuts are acceptable only when they are:
- intentional
- reasonable
- documented

---

## Initial ArchUnit candidates

These are the first rules that can later be enforced with ArchUnit:

### Rule 1
Controllers must not depend on repositories.

### Rule 2
Services must not depend on controllers.

### Rule 3
Repositories must not depend on controllers or services.

### Rule 4
There must be no cycles between top-level packages.

### Rule 5
Classes annotated with `@Entity` must reside in the `entity` package.

These rules are a good starting point because they protect the structure without creating too much friction.

---

## What still belongs to code review

Not everything should be enforced by tooling.

The following should still be reviewed by humans:
- naming quality
- API clarity
- DTO design quality
- exception clarity
- whether a shortcut is justified
- whether responsibilities are truly well placed

Good architecture is not only about passing rules.
It is also about good judgment.

---

## Final principle

Keep the architecture simple, explicit, and easy to maintain.

A good architecture is not the most complex one.
It is the one that makes responsibilities clear and helps the team build with confidence.