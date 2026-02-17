# VeloLand Backend API
Backend REST API built with Spring Boot for an online bicycle shop.
The application manages products, orders, and stock control using PostgreSQL and JPA.
# Technologies Used
- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL
- Maven
- REST API
- Transaction management
# Features
-Product(Create, Read, Update, Delete) operations
-Order Creattion with validation
-Automatic stock reduction
-DTO pattern for request handling
-Total price calculation using Big Decimal
-Layered Architecture
# Architecture Explanation
-Model Layer - Creating Entities
-Repository Layer - Access to database
-Service Layer - Contains business logic
-Controller Layer - Handles HTTP requests
-DTO Layer - Request/Response Objects
