# Library Management System (LMS)
A simple library system built on top of spring boot

## Tech Stack
- Java, Spring Boot
- PostgreSQL
- Docker
- Gradle
- Flyway (embedded)

## Quickstart
### Prerequisites
- Java 17
- Docker

### How to Run?
- build the Jar file
```
./gradlew clean build
```
- run using docker compose
```
docker compose up -d --build
```

### Testing
This project includes unit tests. You can run the test with this command 
```
./gradlew test
```

### API Documentation
This project use OpenAPI (Swagger) as API Documentation. You can check it here  
http://localhost:8080/core/swagger-ui/index.html  


## Configuration
### context path
```
server.servlet.context-path:/core
```
This service default configuration is using context path. Thus, every API can be accessed by this pattern  
```
hostName:port/contextPath/API_PATH
```
here's the curl of get book list API
```
curl --location 'http://localhost:8080/core/books?page=1&size=2' \
--header 'accept: application/json' \
--header 'Content-Type: application/json'
```

### datasource
the datasource configs use environment variables  
e.g. url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:lms_core}
you can modify the config by update the environment that has been set on compose.yaml
```
environment:
  - DB_HOST=db
  - DB_PORT=5432
  - DB_NAME=lms_core
  - DB_USERNAME=postgres
  - DB_PASSWORD=postgres
  - JAVA_OPTS=-Xmx512m -Xms256m
```

## Project Structure
This project use layered architecture, the most basic architecture that suitable for this small project  

## Observability
### Monitoring
This project use Spring boot actuator. You can access the API here
```angular2html
curl --location 'http://localhost:8080/core/actuator' \
--header 'accept: application/json' \
--header 'Content-Type: application/json'
```
In our prod configuration, we override the exposed url to only health.  
If we're using API gateway, we can enable the wildcard to expose all url so that we can access all actuator's APIs and exclude unwanted APIs to be exposed on public  
### Logging
This project implements opentelemetry tracing via micrometer to improve our logging.  
With this, we attach trace ID and span ID to each logs. The trace ID will act as our correlation id.  
By this implementation, we can easily trace and determine all logs related to a single request even when concurrent requests happening.  
This definitely will boost our ability to debug our system flow in real production scenario.  

See the logs below
``` 
2025-12-01T16:02:53.866+07:00  INFO 8986 --- [lms-core-be] [nio-8080-exec-2] [95a2777557b3f2065917e5039ad0ae95-d9bd039f7a9b3351] c.ghifar.lms.core.service.LoanService    : borrowBook() with borrower: 4, book: 2
2025-12-01T16:02:53.902+07:00  INFO 8986 --- [lms-core-be] [nio-8080-exec-2] [95a2777557b3f2065917e5039ad0ae95-d9bd039f7a9b3351] c.ghifar.lms.core.service.LoanService    : book is not available. book: 2, status: BORROWED
2025-12-01T16:02:53.906+07:00 DEBUG 8986 --- [lms-core-be] [nio-8080-exec-2] [95a2777557b3f2065917e5039ad0ae95-d9bd039f7a9b3351] c.g.l.c.config.GlobalExceptionHandler    : handleValidationException() with exception: Book is not available. bookId: 2
2025-12-01T16:02:53.921+07:00  WARN 8986 --- [lms-core-be] [nio-8080-exec-2] [95a2777557b3f2065917e5039ad0ae95-d9bd039f7a9b3351] .m.m.a.ExceptionHandlerExceptionResolver : Resolved [com.ghifar.lms.core.common.exception.ValidationException: Book is not available. bookId: 2]
```

## DB Migration
The naming convention of the migration script file is V{incremental_number}__{script_name}.sql  
You can add your script in
```
src/main/resources/db/migration
```
### Thoughts on Embedded Flyway 
This project still use embedded flyway migration for the sake of delivery
In the future, it's better to separate the migration with the application  
here' the concerns:  
1. Tightly coupled: it is hard to handle rollback scenario <br>
   e.g.: We need to resolve flyway_migration_schema manually before deploying the previous application tag
2. Limited flyway support: With standalone flyway project, we can validate the script first. This will be beneficial since each environment will have different situation. <br>
   e.g.: There might be a case when you add unique constraint, it might success in a ENV but failed in another ENV due to duplicated data
3. Conflicted scripts: Currently we're using incremental versioning. There might be a case when 2 developers working on new script at the same time

## Concurrency handling
There are many ways to resolve this case. I put my reasoning in /docs/opt_lock/result.txt  
Since we already went with optimistic locking, it is better to stick with it rather than implement other option... just for future reference

## Improvements
This is only the initial stage of this API. There are still many thing that we can improve  
1. Implement authentication: <br>
   We can implement simple basic auth / API secret key
2. Add sonarqube to to maintain code quality: <br>
   It is common to implement code coverage > 75%
3. Implement service test: <br>
   For a company that doesn't have QA automation / small automated test coverage, it is a good way to implement service test to maintain credibilty and definitely can help developer to boost the confidence. <br> 
   In Spring Boot, we can utilize test containers
4. Implement CI/CD <br>
   We can improve our pipeline by implementing code quality gate with sonarqube and service test with CI runner.
