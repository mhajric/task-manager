# Task Manager API

## Overview

This project implements a **RESTful task management API** using **Spring Boot**. It focuses on **clear structure, separation of responsibilities, and predictable behavior**, rather than feature completeness.

The system allows users to manage tasks, assign them to users, and receive notifications based on task state, priority, and deadlines.


## Core Capabilities

- Create, update, delete, and retrieve tasks
- Assign tasks to users via email
- Track task priority, status, category, and due date
- Send notifications based on configurable rules
- Handle time-based behavior (e.g. approaching deadlines)


## Task Model

A task contains:
- title, description
- due date
- priority (`Low`, `Medium`, `High`)
- status (`Pending`, `In Progress`, `Completed`)
- category (`Work`, `Personal`, `Other`)
- assigned user (email)

Tasks can be filtered by status and sorted by priority or due date.


## Notifications

- Assignment notifications are sent when a task is created or reassigned
- Additional notifications depend on task priority, status, and due date
- Notification-triggering statuses are configurable and persisted


## API Endpoints

**Tasks**
- `POST /tasks`
- `GET /tasks`
- `PUT /tasks/{id}`
- `DELETE /tasks/{id}`
- `PATCH /tasks/{id}/complete`

**Notification Configuration**
- `POST /config/status-notifications`


## Implementation Notes

- Java 17, Spring Boot, Spring Data JPA
- H2 in-memory database
- Spring Scheduler for time-based behavior
- Email delivery via Spring Mail
- Notifications are scheduled **per task**, not via global polling


## Running the Application

```bash
mvn clean install
./mvnw spring-boot:run
```

SMTP testing (smtp4dev):
```bash
docker run --rm -it -p 3000:80 -p 26:25 rnwood/smtp4dev:v3
```

Swagger UI:
http://localhost:8080/swagger-ui/index.html


## Demo Users

- **user** / password
- **admin** / password


## Notes

This repository emphasizes **design clarity and behavior**, not exhaustive edge-case handling.


## License

MIT License

