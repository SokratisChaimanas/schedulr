# Schedulr

Schedulr is an event management application featuring authentication and authorization, with extended administrative capabilities. The frontend is built as a Single Page Application (SPA) using Angular, while the backend follows a Service-Oriented Architecture (SOA) using Spring Boot.

## Deployment Guide

### Cloning the Repository
To securely obtain the project from GitHub, use the following command:
```sh
git clone --depth=1 https://github.com/SokratisChaimanas/schedulr.git
```
This ensures that only the latest commit is fetched, minimizing exposure to unnecessary history.

### Backend Setup (Spring Boot)
#### Prerequisites:
- Java 17+
- Gradle
- MySQL Database

#### Configuration
Ensure the following properties are correctly set in `application.properties` to read from the environment:
```properties
spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DB}?serverTimezone=UTC
spring.datasource.username=${MYSQL_USER}
spring.datasource.password=${MYSQL_PASSWORD}
```

#### Build and Run Backend
```sh
cd backend/schedulr
gradle wrapper
./gradlew build
./gradlew bootRun
```
> **Once the database is up, make sure to run the `event_scheduler.sql` script located in the `resources` directory.**  
> This will handle the update of the events' status on a database level.

After the backend server is up and running, you can access the API documentation via Swagger at:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Backend Structure
The backend follows a structured approach under the `schedulr` package:
- **authentication** - Handles authentication and authorization logic, including login, registration, and JWT security.
- **core** - Contains configurations, enums, exception handling, filters, mappers, and utility functions.
- **dto** - Defines Data Transfer Objects used for communication between layers.
- **model** - Represents the entity models mapped to the database.
- **repository** - Houses the interfaces for interacting with the database.
- **rest** - Contains the controllers for handling API requests and responses.
- **security** - Manages security configurations and user permissions.
- **service** - Implements business logic and interaction between layers.
- **specification** - Provides query specifications for dynamic database queries.
- **validation** - Contains custom validation logic and constraints.

### Frontend Setup (Angular)
#### Prerequisites:
- Node.js 18+
- Angular CLI

#### Install Dependencies and Build
```sh
cd frontend/schedulr
npm install
ng serve
```

### Frontend Structure
The frontend follows a modular structure within the `src/app` directory:
- **components** - Holds UI components that are reusable across the application.
- **shared** - Contains:
  - **guards** - Handles route protection and authentication checks.
  - **interceptors** - Manages HTTP requests, including authorization tokens.
  - **interfaces** - Defines TypeScript interfaces for structured data management.
  - **services** - Provides services for API interaction, authentication, and data handling.
- **index.html** - The main HTML file that serves as the entry point for the application.
- **main.ts** - The main TypeScript file responsible for bootstrapping the Angular application.
- **styles.css** - The global styles file for the frontend.

The `public` folder contains necessary static assets such as images and icons.

## Additional Considerations
- Ensure MySQL is running and properly configured.
- If using environment variables, ensure they are exported or set before running the application.
- The backend should be deployed before the frontend for seamless integration.
- Backend runs on [localhost:8080](http://localhost:8080/)
- Frontend runs on [http://localhost:4200/](http://localhost:4200/)

## Project Overview

Once up and running, users can interact with the system in the following ways:

- **User Registration and Authentication**: Users can create accounts using the registration screen and log in to access the app.
- **Homepage**: Displays a list of pending events (initially empty for new users).
- **Creating Events**: Users can create new events through the "Create Event" tab.
- **My Events**: Shows all events created by the logged-in user.
- **My Attends**: Displays events the user has chosen to attend.
- **Event Interaction**: Users can:
  - Leave comments on events.
  - Mark attendance to events.
- **Admin Features**:
  - Moderate comments and events.
  - View all events regardless of their status.
