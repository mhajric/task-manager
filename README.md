# task-manager


Build a RESTful API for a task management system using Spring Boot.
The API should allow users to create, update, delete, and manage tasks.
Each task has a due date, priority, status, category, and an assigned user.
Implement a notification system based on task priorities and statuses.

## Requirements:
### User Story:
As a user, I want to:
1. Create a task with a title, description, due date, priority (Low, Medium, High), status (Pending, In Progress, Completed), category (Work, Personal, Others),
   and an assigned user (email).
2. Update a task (e.g., change status, due date, etc.).
3. Delete a task.
4. Retrieve all tasks, filter by status, and sort by priority or due date.
5. Mark a task as completed.
6. Receive notifications based on the task’s priority and status.

### Additional Features:
- Task Assignment Email:
  When a task is created or assigned, an email notification must be sent to the user responsible for the task.
- Scheduled Job (Cron):
  Implement a scheduled job that checks for tasks with approaching deadlines or tasks that need attention based on priority and status. Notifications should be sent to
  the assigned users periodically.
- Priority-based Notifications:
  The frequency of the notifications should be based on the task’s priority. Higher priority tasks should trigger more frequent notifications.
- Configurable Status Notifications:
  The application should allow the admin (or app creator) to configure which task statuses trigger notifications (e.g., notifications when the task is "In Progress" or "Overdue").

### Endpoints:
POST /tasks: Create a new task.
GET /tasks: Retrieve all tasks with optional filtering and sorting.
PUT /tasks/{id}: Update a task.
DELETE /tasks/{id}: Delete a task.
PATCH /tasks/{id}/complete: Mark a task as completed.
POST /config/status-notifications: Configure which statuses trigger notifications.

### Technologies:
Use Spring Boot for developing the API.
Use JPA/Hibernate for data persistence.
Use Spring Mail for sending email notifications.
Use Spring Scheduler to create a cron job for periodic checks and notifications.
Store data in an H2 in-memory database for simplicity.
Use Lombok for reducing boilerplate code (optional).

### Architecture:
Design the API using clean architecture principles. Organize the project into services, controllers, and repositories.
Use DTOs (Data Transfer Objects) for incoming and outgoing data models.
Ensure proper exception handling and meaningful error messages.
The configuration of notification-triggering statuses should be stored in the database and accessible via a configurable endpoint.

### Bonus Points:
Implement unit tests for service and repository layers using JUnit and Mockito.
Use Swagger for API documentation.
Implement basic authentication (Spring Security).
Handle pagination for task retrieval when there are many tasks.

## Getting Started

To build project and run tests, run:
`mvn clean install`

Then run app using:
`./mvnw spring-boot:run`

Access swagger-ui:
http://localhost:8080/swagger-ui/index.html