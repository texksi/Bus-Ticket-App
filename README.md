# Bus Ticket App

A REST API for managing bus tickets, reservations, and payments, built with Spring Boot.

## Tech Stack

- Java 17
- Spring Boot 4.0.5
- PostgreSQL
- Hibernate / Spring Data JPA
- MapStruct
- Lombok
- Stripe (payments)
- Springdoc OpenAPI (Swagger UI)

## How to run the application (steps)

### Prerequisites

- Java 17+
- PostgreSQL
- Maven

### Setup

1. Clone the repository
2. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE busticket;
   ```
3. Set the following environment variables (or add them to your run configuration):
   ```
   DB_URL=jdbc:postgresql://localhost:5432/busticket
   DB_USER=your_db_user
   DB_PASSWORD=your_db_password
   STRIPE_SECRET_KEY=sk_test_your_key
   ```
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The app will start on `http://localhost:8080`.

## API Documentation

Swagger UI is available at:
```
http://localhost:8080/swagger-ui/index.html
```

## Running Tests

```bash
./mvnw test
```

## Domain Overview

| Entity | Description |
|---|---|
| `Korisnik` | Application user |
| `Kompanija` | Bus company |
| `Vozilo` | Vehicle belonging to a company |
| `Putovanje` | Trip operated by a company using a vehicle |
| `Rezervacija` | Reservation made by a user |
| `Karta` | Ticket linked to a reservation and a trip |
| `Ocena` | User rating for a trip |
| `Placanje` | Stripe payment linked to a reservation |