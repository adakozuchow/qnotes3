# Qnotes3

A modern note-taking application built with Spring Boot and Angular, featuring user authentication, note management, and statistics tracking.

## Features

- **User Authentication**
  - JWT-based authentication
  - User registration and login
  - Secure password handling

- **Note Management**
  - Create, read, update, and delete notes
  - Priority levels (NOW, LATER, SOMEDAY, DONE)
  - Soft deletion of notes
  - Pagination and filtering options

- **Statistics**
  - Track stale notes (not updated in 2 days)
  - Count high-priority notes
  - Calculate average completion time
  - Calculate average deletion time

## Technology Stack

### Backend
- Java 21
- Spring Boot 3.5.4
- Spring Security
- MongoDB
- JWT Authentication
- Maven

### Frontend
- Angular 17
- Angular Material
- RxJS
- TypeScript
- Cypress (E2E Testing)

## Prerequisites

- Java 21 or higher
- Node.js 18 or higher
- MongoDB (or Docker for containerized MongoDB)
- Maven 3.x

## Local Development Setup

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd be
   ```

2. Install dependencies:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd fe
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

The frontend will be available at `http://localhost:4200`

## Running Tests

### Backend Tests

The backend uses JUnit and Mockito for testing. To run the tests:

```bash
cd be
mvn test
```

Key test classes:
- `NoteServiceTest`: Tests for note management functionality
- `JwtServiceTest`: Tests for JWT token handling
- `UserServiceTest`: Tests for user authentication
- Integration tests for authentication and security

### Frontend Tests

The frontend uses Cypress for E2E testing. To run the tests:

1. Make sure both backend and frontend are running
2. Open Cypress Test Runner:
   ```bash
   cd fe
   npm run cypress:open
   ```

3. Run tests in headless mode:
   ```bash
   npm run cypress:run
   ```

## API Endpoints

### Authentication
- `POST /api/auth/register`: Register new user
- `POST /api/auth/login`: Login user

### Notes
- `GET /api/notes`: Get paginated notes
- `POST /api/notes`: Create new note
- `GET /api/notes/{id}`: Get specific note
- `PUT /api/notes/{id}`: Update note
- `DELETE /api/notes/{id}`: Delete note (soft delete)

### Statistics
- `GET /api/statistics/stale`: Count stale notes
- `GET /api/statistics/high-priority`: Count high priority notes
- `GET /api/statistics/completion-time`: Get average completion time
- `GET /api/statistics/deletion-time`: Get average deletion time

## Project Structure

### Backend Structure
```
be/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ydgrun/info/qnotes3/
│   │   │       ├── api/
│   │   │       ├── config/
│   │   │       ├── controllers/
│   │   │       ├── domain/
│   │   │       ├── exception/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       └── service/
│   │   └── resources/
│   └── test/
└── pom.xml
```

### Frontend Structure
```
fe/
├── src/
│   ├── app/
│   │   ├── core/
│   │   │   ├── guards/
│   │   │   ├── interceptors/
│   │   │   └── services/
│   │   ├── features/
│   │   │   ├── auth/
│   │   │   ├── notes/
│   │   │   └── statistics/
│   │   └── shared/
│   └── assets/
├── cypress/
└── package.json
```

## Security

- JWT-based authentication
- Password encryption using BCrypt
- CORS configuration
- HTTP-only cookies
- Input validation
- Error handling

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
