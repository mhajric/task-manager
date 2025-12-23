# Task Manager API

This project implements a RESTful API for a task management system using Spring Boot.  
It was developed as a challenge assignment with a focus on clean design, clear separation of responsibilities, and extensibility.

The system allows users to create and manage tasks, assign them to users, and receive notifications based on task priority, status, and deadlines.

---

## Functional Overview

The application supports the following core capabilities:

- Task creation, update, deletion, and retrieval
- Task assignment to users via email
- Task categorization, prioritization, and status tracking
- Notification delivery based on configurable rules
- Scheduled checks for time-based conditions (e.g. approaching deadlines)

---

## User Requirements

As a user, the system allows me to:

1. Create a task with:
  - title
  - description
  - due date
  - priority (Low, Medium, High)
  - status (Pending, In Progress, Completed)
  - category (Work, Personal, Other)
  - assigned user (email)
2. Update task attributes (status, due date, priority, etc.)
3. Delete a task
4. Retrieve tasks with filtering by status and sorting by priority or due date
5. Mark a task as completed
6. Receive notifications related to task state and urgency

---

## Notification Features

### Task Assignment Notification
When a task is created or assigned to a user, an email notification is sent to the assigned user.

### Priority and Status-Based Notifications
Notifications are triggered based on:
- task priority
- task status
- proximity to due date

Higher-priority tasks generate more frequent notifications.

### Configurable Notification Rules
The application allows configuration of which task statuses should trigger notifications (e.g. `In Progress`, `Overdue`).  
These rules are stored in the database and can be updated via an API endpoint.

---

## REST API Endpoints

### Task Management
- `POST /tasks` – Create a new task
- `GET /tasks` – Retrieve tasks (supports filtering and sorting)
- `PUT /tasks/{id}` – Update a task
- `DELETE /tasks/{id}` – Delete a task
- `PATCH /tasks/{id}/complete` – Mark task as completed

### Notification Configuration
- `POST /config/status-notifications` – Configure which task statuses trigger notifications

---

## Technical Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA (Hibernate)
- Spring Scheduler
- Spring Mail
- H2 in-memory database
- Lombok (optional)
- Swagger / OpenAPI

---

## Architecture

The application follows clean architecture principles:

- **Controller layer**  
  Exposes REST endpoints and handles request/response mapping and validation.

- **Service layer**  
  Contains application use cases and business logic.  
  Responsible for coordinating task management, notification triggers, and scheduling.

- **Repository layer**  
  Handles data persistence using JPA/Hibernate.

DTOs are used to separate API models from internal domain models.  
Exceptions are handled centrally and mapped to meaningful HTTP responses.

---

## Scheduled Processing

The system includes scheduled logic to handle time-based behavior:

- Periodic checks for tasks approaching their due date
- Notification scheduling based on task priority and status

Instead of relying on a single global scheduled job that polls all tasks, notifications are scheduled per task.  
This approach simplifies notification timing logic and avoids complex recalculation windows.

On application startup or notification configuration changes, all persisted tasks are rescheduled accordingly.

---

## Design Considerations

During domain analysis, two main concerns were identified:

1. **Task Management**  
   Straightforward CRUD operations with additional business rules around status transitions and assignment.

2. **Notification Handling**  
   Requires careful consideration to avoid duplicate or missed notifications.

### Applied Approach

- Assignment and creation notifications are sent synchronously.
- Time-based and priority-based notifications are scheduled per task.
- Notification rules are configurable and persisted.
- On application restart, scheduled notifications are reconstructed from persisted state.

Certain edge cases (e.g. extended application downtime) are acknowledged but intentionally left out for simplicity.

---

## Getting Started

### Prerequisites
- Java 17
- Docker (for smtp4dev)
- Git

### Clone Repository
```bash
git clone git@github.com:mhajric/task-manager.git
```

### Build and Test
```bash
mvn clean install
```

### Start SMTP Test Server
```bash
docker run --rm -it -p 3000:80 -p 26:25 rnwood/smtp4dev:v3
```

Access smtp4dev UI:  
http://localhost:3000

### Run Application
```bash
./mvnw spring-boot:run
```

Swagger UI:  
http://localhost:8080/swagger-ui/index.html

---

## Demo Users

Two users are preconfigured:

- **user** / password
- **admin** / password

Use the admin account to configure notification rules, then create tasks assigned to the user and verify email delivery via smtp4dev.