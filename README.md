# Task Manager

A RESTful API for managing tasks using Spring Boot. The API enables users to create, update, delete, and manage tasks, each with attributes like due date, priority, status, category, and assigned user. Notifications are sent based on task priority and status.

## Requirements

### User Stories
- **Create** tasks with title, description, due date, priority, status, category, and assigned user.
- **Update** task details.
- **Delete** tasks.
- **Retrieve** all tasks with filtering/sorting.
- **Mark** tasks as completed.
- **Receive Notifications** based on task priority and status.

### Features
- **Task Assignment Email**: Notify users via email upon task creation/assignment.
- **Scheduled Notifications**: Cron job to alert users based on deadlines and priority.
- **Priority-based Notifications**: Higher priority triggers more frequent notifications.
- **Configurable Status Notifications**: Admin-configurable notifications based on task status.

### API Endpoints
- `POST /tasks`: Create a new task.
- `GET /tasks`: Retrieve tasks with filtering/sorting options.
- `PUT /tasks/{id}`: Update a task.
- `DELETE /tasks/{id}`: Delete a task.
- `PATCH /tasks/{id}/complete`: Mark a task as completed.
- `POST /config/status-notifications`: Configure notification-triggering statuses.

### Technologies
- **Backend**: Spring Boot, Spring Scheduler, JPA/Hibernate.
- **Notifications**: Spring Mail for email, cron-based scheduling.
- **Database**: H2 (in-memory).
- **Utility**: Lombok for reducing boilerplate.

### Architecture
- **Clean Architecture** with separate layers for services, controllers, and repositories.
- **DTOs** for data models and **exception handling** for meaningful errors.
- **Configurable Status Notifications**: Stored in the database and managed via endpoint.

### Bonus Points
- **Testing**: Unit tests with JUnit and Mockito.
- **Documentation**: Swagger for API documentation.
- **Security**: Basic authentication with Spring Security.
- **Pagination**: For efficient task retrieval in large datasets.


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

The system consists of two primary subdomains:

1. **Task Management**: Provides CRUD operations for tasks. CRUD events like task creation and assignment trigger notifications. Input and business validation should ensure data consistency. 
  TODO: A scheduled job also changes task statuses to "overdue" based on current time.

2. **Notification System**: The notification system requires different approaches based on trigger events and priority configurations:

  - **New and Assigned Tasks**: Notifications fire synchronously upon task creation or assignment.
  - **Status and Priority-based Notifications**: Instead of using a global `@Scheduled` method, task-specific scheduled jobs handle notifications as needed based on task attributes.
  - **Configuration Changes**: When notification configurations are modified, all stored tasks are re-scheduled according to the latest configuration. (TODO: application restarts)

   > *Note*: The solution does not yet cover downtime scenarios, but re-syncing notifications on application restart could address this.
