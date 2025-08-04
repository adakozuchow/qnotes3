# Product Requirements Document - Qnotes2 MVP

## 1. Project Overview
Simple note-taking application with priority management.

## 2. Core Features
1. User registration and authentication
2. Note management (CRUD operations)
3. Soft delete with TTL cleanup handled by MongoDB engine

## 4. Technical Requirements
1. RESTful API with basic JWT authentication
2. Local MongoDB rised in spring context for data storage
3. Angular for web-platform client
4. Automated tests (unit, integration)
5. CI/CD pipeline with GitHub Actions
6. Swagger/OpenAPI 3.1 documentation

## 5. Limitations and Constraints
- No push notifications, calendar integrations, or advanced permission management
- No note versioning support
- No encryption or WCAG compliance requirements
- Only simple JWT authentication with no support for refresh tokens
- Timeline: MVP release within 3 weeks from project start
- Team: 1 backend Java developer, 1 junior frontend developer

## 6s. Success Metrics
1. Dashboard counters update on note status change, deletion, or creation
2. MVP delivered within 3-week timeline
3. Public GitHub repository with setup documentation 