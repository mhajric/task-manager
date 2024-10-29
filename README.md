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

Requirements:
- Java (version 17)
- Docker (in order to run smtp4dev)
- git

Clone this repo:  
`git clone git@github.com:mhajric/task-manager.git`

To build project and run tests, run:  
`mvn clean install`

Start smtp4dev:  
`docker run --rm -it -p 3000:80 -p 26:25 rnwood/smtp4dev:v3`

Open smtp4dev web interface:  
http://localhost:3000

Then run app using:  
`./mvnw spring-boot:run`

Access swagger-ui:
http://localhost:8080/swagger-ui/index.html

There are two users registered: '**user**' and '**admin**' (both passwords are '**password**').  

You should update config using admin user and then create `Task` using user as username. Verify email is sent in smtp4dev.

## Design considerations

Analysing domain I found two subdomains: Task management and Notifications. 
Task management is straight-forward implementation of CRUD service where some CRUD operations can trigger notifications.  
Note: input validation and business validation should be implemented. Additionally, there should be scheduled job to change status of tasks to overdue based on current time.  

Implementing Notification System can be done in multiple ways. One naive approach could be to have @Scheduled method to run every minute, fetch tasks and send notifications. 
I found this solution could lead to complex notification trigger calculations and possible issues like out-of-period notifications based on task creation time.

Applied solution:
For new tasks and for just assigned tasks, new notification is fired synchronously.
Instead of having @Scheduled method for notifications based on status and priority (and also for tasks which due date is soon), scheduled jobs are created on Task basis.
In case of notification config change and application restart, all persisted tasks are rescheduled.  
Note: corner case like application down-time are not handled even it could be trivial to re-sync status and priority based notifications.
