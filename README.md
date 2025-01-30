# Schedulr

Schedulr is an event management application featuring authentication and authorization, with extended administrative capabilities. The frontend is built as a Single Page Application (SPA) using Angular, while the backend follows a Service-Oriented Architecture (SOA) using Spring Boot.

## Deployment Guide

### Cloning the Repository
To securely obtain the project from GitHub, use the following command:
```sh
git clone --depth=1 <REPO_URL>
```
This ensures that only the latest commit is fetched, minimizing exposure to unnecessary history.

### Backend Setup (Spring Boot)
#### Prerequisites:
- Java 17+
- Gradle
- MySQL Database

#### Configuration
Ensure the following properties are correctly set in `application.properties`:
```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.globally_quoted_identifiers=true
spring.data.jpa.repositories.enabled=true

spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=10MB

# DB CONNECTION
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DB:schedulrdb}?serverTimezone=UTC
spring.datasource.username=${MYSQL_USER:schedulruser}
spring.datasource.password=${MYSQL_PASSWORD:aufhqnWe9.FW48_Xp6oJ}
spring.jpa.hibernate.ddl-auto=update

spring.sql.init.encoding=UTF-8
spring.sql.init.platform=mysql

logging.level.org.springframework.security=DEBUG
```

#### Build and Run Backend
```sh
./gradlew build
java -jar build/libs/schedulr.jar
```

### Frontend Setup (Angular)
#### Prerequisites:
- Node.js 18+
- Angular CLI

#### Install Dependencies and Build
```sh
cd frontend
npm install
ng build --configuration=production
```

## Additional Considerations
- Ensure MySQL is running and properly configured.
- Environment variables can be used to override sensitive database credentials.
- The backend should be deployed before the frontend for seamless integration.

More details will be added as needed.
