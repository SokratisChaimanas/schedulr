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

## Additional Considerations
- Ensure MySQL is running and properly configured.
- If using environment variables, ensure they are exported or set before running the application.
- The backend should be deployed before the frontend for seamless integration.
- Backend runs on [localhost:8080](http://localhost:8080/)
- Frontend runs on [http://localhost:4200/](http://localhost:4200/)

More details will be added as needed.
