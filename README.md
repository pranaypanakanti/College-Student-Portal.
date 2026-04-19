# 🎓 College Student Portal

A complete **College Management REST API** built with **Spring Boot 3.2**, **Spring Security (JWT)**, **Spring Data JPA**, and **PostgreSQL**.

## 🛠 Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.2 |
| Security | Spring Security, JWT (jjwt 0.12.6) |
| Database | PostgreSQL |
| ORM | Spring Data JPA (Hibernate) |
| Docs | Swagger / OpenAPI (springdoc) |
| Build | Maven |
| Deployment | Render / Docker |

## 📁 Project Structure

```
src/main/java/com/college/student/portal/
├── config/          → Swagger configuration
├── controller/      → REST Controllers
├── dto/             → Data Transfer Objects
├── entity/          → JPA Entities (11 tables)
├── enums/           → Role, AttendanceStatus, etc.
├── exception/       → Custom exceptions + Global handler
├── repository/      → JPA Repositories
├── security/        → JWT, Filter, Security Config
└── service/         → Business Logic
```

## 🗄 Database Schema

11 Tables: Student, Faculty_Personal, Admin, Subject, Course, Enrollment, Attendance, Marks, Fee_Structure, Fee_Payment, Announcement

## 🔑 API Endpoints

### Auth (Public)
- `POST /api/auth/student/register`
- `POST /api/auth/student/login`
- `POST /api/auth/faculty/register`
- `POST /api/auth/faculty/login`
- `POST /api/auth/admin/register`
- `POST /api/auth/admin/login`

### Student (Requires JWT)
- `GET /api/students/me`
- `PATCH /api/students/{id}`
- `PUT /api/students/change-password`

### Admin
- `GET /api/admin/students`
- `GET /api/admin/students/filter?branch=CSE&semester=1`

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.9+
- PostgreSQL 15+

### Setup
```bash
git clone https://github.com/pranaypanakanti/College-Student-Portal.git
cd College-Student-Portal
```

### Create Database
```sql
CREATE DATABASE college_student_portal;
```

### Configure
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/college_student_portal
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

### Run
```bash
mvn spring-boot:run
```

### Test
```bash
mvn test
```

### Swagger UI
```
http://localhost:8080/swagger-ui.html
```
