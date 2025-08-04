# REST API Plan - Qnotes3 MVP

## 1. Overview

This document defines the REST API specification for Qnotes3 MVP - a lightweight, cross-platform application for managing prioritized notes/tasks. The API is built using Java Spring Boot with MongoDB as the database backend.

## 2. Core Resources

- **Users** (`users` collection) - User data, authentication, and preferences
- **Notes** (`notes` collection) - Primary business data - notes/tasks with priorities  
- **Auth Tokens** (`refreshTokens` collection) - JWT session management
- **User Statistics** (`userStats` collection) - Dashboard counters for priority statistics

## 3. API Endpoints

### 3.1 Authentication Endpoints

#### POST /api/auth/register
**Description**: Register a new user account  
**Authentication**: None required  
**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "securePassword123",
  "language": "en"
}
```
**Success Response (201)**:
```json
{
  "message": "User registered successfully",
  "user": {
    "id": "507f1f77bcf86cd799439011",
    "email": "user@example.com", 
    "language": "en",
    "createdAt": "2024-01-15T10:30:00Z"
  }
}
```
**Error Responses**:
- `400 Bad Request` - Invalid email format, weak password, or email already exists
- `422 Unprocessable Entity` - Validation errors

#### POST /api/auth/login
**Description**: User authentication with email and password  
**Authentication**: None required  
**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "securePassword123"
}
```
**Success Response (200)**:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": "507f1f77bcf86cd799439011",
    "email": "user@example.com",
    "language": "en"
  }
}
```
**Error Responses**:
- `401 Unauthorized` - Invalid credentials
- `400 Bad Request` - Missing email or password

#### POST /api/auth/refresh
**Description**: Refresh access token using refresh token  
**Authentication**: None required  
**Request Body**:
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
**Success Response (200)**:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer", 
  "expiresIn": 3600
}
```
**Error Responses**:
- `401 Unauthorized` - Invalid or expired refresh token
- `400 Bad Request` - Missing refresh token

#### POST /api/auth/logout
**Description**: Logout user and revoke refresh token  
**Authentication**: Bearer token required  
**Request Body**:
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
**Success Response (200)**:
```json
{
  "message": "Logged out successfully"
}
```
**Error Responses**:
- `401 Unauthorized` - Invalid access token
- `400 Bad Request` - Missing refresh token

#### POST /api/auth/logout-all
**Description**: Logout from all devices by revoking all user's refresh tokens  
**Authentication**: Bearer token required  
**Request Body**: Empty  
**Success Response (200)**:
```json
{
  "message": "Logged out from all devices successfully"
}
```
**Error Responses**:
- `401 Unauthorized` - Invalid access token

### 3.2 Notes Management

#### GET /api/notes
**Description**: Retrieve user's notes with sorting, filtering, and pagination  
**Authentication**: Bearer token required  
**Query Parameters**:
- `sort` (optional): `priority` (default), `date`, `createdAt`, `updatedAt`
- `order` (optional): `asc`, `desc` (default for date)
- `page` (optional): page number (default: 0)
- `size` (optional): page size (default: 20, max: 100)
- `priority` (optional): filter by priority value [`NOW`, `LATER`, `SOMEDAY`, `DONE`]
- `search` (optional): search in note descriptions (case-insensitive)

**Success Response (200)**:
```json
{
  "content": [
    {
      "id": "507f1f77bcf86cd799439011",
      "description": "Buy groceries",
      "priority": "NOW",
      "createdAt": "2024-01-15T10:30:00Z",
      "updatedAt": "2024-01-15T10:30:00Z",
      "completedAt": null
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "empty": false,
      "orders": [
        {
          "property": "priority",
          "direction": "ASC"
        }
      ]
    },
    "pageSize": 20,
    "pageNumber": 0,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 45,
  "totalPages": 3,
  "last": false,
  "first": true,
  "numberOfElements": 20,
  "size": 20,
  "number": 0,
  "empty": false
}
```
**Error Responses**:
- `401 Unauthorized` - Invalid or missing access token
- `400 Bad Request` - Invalid query parameters

#### POST /api/notes
**Description**: Create a new note  
**Authentication**: Bearer token required  
**Request Body**:
```json
{
  "description": "Buy groceries",
  "priority": "NOW"
}
```
**Success Response (201)**:
```json
{
  "id": "507f1f77bcf86cd799439011",
  "description": "Buy groceries", 
  "priority": "NOW",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```
**Error Responses**:
- `401 Unauthorized` - Invalid or missing access token
- `400 Bad Request` - Invalid priority or description length
- `422 Unprocessable Entity` - Validation errors

#### GET /api/notes/{id}
**Description**: Retrieve a specific note by ID  
**Authentication**: Bearer token required  
**Path Parameters**:
- `id`: Note ID (MongoDB ObjectId)

**Success Response (200)**:
```json
{
  "id": "507f1f77bcf86cd799439011",
  "description": "Buy groceries",
  "priority": "NOW", 
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```
**Error Responses**:
- `401 Unauthorized` - Invalid or missing access token
- `404 Not Found` - Note not found or access denied
- `400 Bad Request` - Invalid note ID format

#### PUT /api/notes/{id}
**Description**: Update an existing note (full replacement)  
**Authentication**: Bearer token required  
**Path Parameters**:
- `id`: Note ID (MongoDB ObjectId)

**Request Body**:
```json
{
  "description": "Buy groceries and milk",
  "priority": "LATER"
}
```
**Success Response (200)**:
```json
{
  "id": "507f1f77bcf86cd799439011",
  "description": "Buy groceries and milk",
  "priority": "LATER",
  "createdAt": "2024-01-15T10:30:00Z", 
  "updatedAt": "2024-01-15T11:45:00Z"
}
```
**Error Responses**:
- `401 Unauthorized` - Invalid or missing access token
- `404 Not Found` - Note not found or access denied
- `400 Bad Request` - Invalid priority, description length, or note ID format
- `422 Unprocessable Entity` - Validation errors

#### DELETE /api/notes/{id}
**Description**: Delete a note (soft delete with TTL)  
**Authentication**: Bearer token required  
**Path Parameters**:
- `id`: Note ID (MongoDB ObjectId)

**Success Response (204)**:
```
(Empty response body)
```
**Error Responses**:
- `401 Unauthorized` - Invalid or missing access token
- `404 Not Found` - Note not found or access denied
- `400 Bad Request` - Invalid note ID format

### 3.3 Business Functions

#### GET /api/notes/filters/date
**Description**: Filter notes by date range as specified in business requirements  
**Authentication**: Bearer token required  
**Query Parameters**:
- `dateRange`: Enum [`TODAY`, `PAST_SEVEN_DAYS`, `ALL`] (required)
- `page` (optional): page number (default: 0)
- `size` (optional): page size (default: 20, max: 100)

**Success Response (200)**:
```json
{
  "content": [
    {
      "id": "507f1f77bcf86cd799439011",
      "description": "Buy groceries",
      "priority": "NOW",
      "createdAt": "2024-01-15T10:30:00Z",
      "updatedAt": "2024-01-15T10:30:00Z"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "empty": false
    },
    "pageSize": 20,
    "pageNumber": 0,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 45,
  "totalPages": 3
}
```

#### GET /api/notes/stats/stale
**Description**: Get count of notes that haven't been updated in more than 2 days (excluding DONE status)  
**Authentication**: Bearer token required  

**Success Response (200)**:
```json
{
  "count": 5,
  "asOfDate": "2024-01-15T12:00:00Z"
}
```

#### GET /api/notes/stats/completion-time
**Description**: Get note completion time analysis  
**Authentication**: Bearer token required  
**Query Parameters**:
- `type` (optional): [`DONE`, `DELETED`] - defaults to both if not specified

**Success Response (200)**:
```json
{
  "averageTimeToComplete": {
    "done": {
      "hours": 48.5,
      "totalNotes": 100
    },
    "deleted": {
      "hours": 72.3,
      "totalNotes": 50
    }
  },
  "asOfDate": "2024-01-15T12:00:00Z"
}
```

### 3.4 User Profile Management

#### GET /api/users/profile
**Description**: Get current user's profile information  
**Authentication**: Bearer token required  

**Success Response (200)**:
```json
{
  "id": "507f1f77bcf86cd799439011",
  "email": "user@example.com",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```
**Error Responses**:
- `401 Unauthorized` - Invalid or missing access token

#### PUT /api/users/profile
**Description**: Update user profile  
**Authentication**: Bearer token required  
**Request Body**:
```json
{
  "email": "newemail@example.com"
}
```
**Success Response (200)**:
```json
{
  "id": "507f1f77bcf86cd799439011",
  "email": "newemail@example.com",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T12:00:00Z"
}
```
**Error Responses**:
- `401 Unauthorized` - Invalid or missing access token
- `400 Bad Request` - Invalid email format
- `422 Unprocessable Entity` - Validation errors

#### GET /api/users/stats
**Description**: Get user's dashboard statistics (priority counters)  
**Authentication**: Bearer token required  

**Success Response (200)**:
```json
{
  "nowCount": 5,
  "laterCount": 12, 
  "somedayCount": 3,
  "doneCount": 8,
  "updatedAt": "2024-01-15T12:00:00Z"
}
```
**Error Responses**:
- `401 Unauthorized` - Invalid or missing access token

### 3.5 Health Check

#### GET /api/health
**Description**: API health check endpoint  
**Authentication**: None required  

**Success Response (200)**:
```json
{
  "status": "UP",
  "timestamp": "2024-01-15T12:00:00Z"
}
```

## 4. Authentication & Authorization

### 4.1 Authentication Mechanism
- **JWT Bearer Token Authentication** with access/refresh token pair
- **Access Token**: Short-lived (1 hour), contains user information
- **Refresh Token**: Long-lived (7 days), stored in database with TTL
- **Spring Security** with custom JWT filters for token validation

### 4.2 Token Details
1. **Access Token** contains: `userId`, `email`, `language`, `exp`, `iat`
2. **Refresh Token** is unique, stored in database with `expiresAt` and `isRevoked` flags
3. **Token Rotation**: Each refresh generates new token pair
4. **Automatic Cleanup**: TTL index in MongoDB removes expired tokens
5. **Logout**: Revokes refresh token by setting `isRevoked=true`

### 4.3 Authorization Headers
```
Authorization: Bearer <access_token>
```

### 4.4 Security Considerations
- **Password Hashing**: BCrypt with salt rounds
- **Token Security**: JWT signed with HMAC SHA-256
- **CORS Configuration**: Restrict origins in production
- **Rate Limiting**: 100 requests/minute per user, 10 auth requests/minute per IP
- **Input Validation**: Strict validation on all endpoints
- **SQL Injection Prevention**: MongoDB parameterized queries

## 5. Data Validation & Business Logic

### 5.1 Input Validation Rules

#### User Registration/Profile
- **Email**: Valid email format, unique constraint, max 254 characters
- **Password**: Minimum 8 characters, must contain letters and numbers

#### Note Management
- **Description**: Required, 1-300 characters, non-empty after trim
- **Priority**: Enum values ["NOW", "LATER", "SOMEDAY", "DONE"], default "NOW"
- **Ownership**: Notes can only be accessed/modified by their owner

### 5.2 Business Logic Implementation

#### Note Operations
1. **Create**: Auto-set `createdAt`, `updatedAt`, `userId` from JWT token
2. **Update**: Update `updatedAt`, validate ownership
3. **Delete**: Soft delete - set `deleted=true`, `deletedAt=now()`
4. **Priority "DONE"**: Auto-set `completedAt` for TTL cleanup

#### Dashboard Statistics (userStats)
- **Automatic Updates**: Every note CRUD operation updates relevant counters
- **Atomic Operations**: Use MongoDB `$inc` operators for thread-safety
- **Initialization**: Create userStats on user's first note creation
- **Consistency**: Periodic background job to verify counter accuracy

#### TTL (Time To Live) Cleanup
- **Soft-deleted notes**: Auto-remove after 7 days from `deletedAt`
- **Completed notes**: Auto-remove after 7 days from `completedAt`
- **Expired tokens**: Auto-remove when `expiresAt` reached

#### Priority Sorting (Default Order)
Priority order: "NOW" → "LATER" → "SOMEDAY" → "DONE"
```javascript
// MongoDB sort implementation
{
  $addFields: {
    priorityOrder: {
      $switch: {
        branches: [
          { case: { $eq: ["$priority", "NOW"] }, then: 1 },
          { case: { $eq: ["$priority", "LATER"] }, then: 2 },
          { case: { $eq: ["$priority", "SOMEDAY"] }, then: 3 },
          { case: { $eq: ["$priority", "DONE"] }, then: 4 }
        ]
      }
    }
  }
}
```

#### Business Function Implementation
1. **Date Range Filtering**:
   - TODAY: MongoDB query with `$gte` on midnight of current day
   - PAST_SEVEN_DAYS: `$gte` on date 7 days ago
   - ALL: No date filter, only pagination

2. **Stale Notes Counter**:
   - MongoDB aggregation comparing `updatedAt` with current time
   - `$match` stage excludes "DONE" status
   - Uses `$lt` operator for 2+ days old check

3. **Completion Time Analysis**:
   - Separate aggregation pipelines for DONE and DELETED notes
   - Calculate average using `$avg` on time difference
   - Handle timezone considerations in calculations
   - Cache results with 1-hour TTL for performance

## 6. Error Handling

### 6.1 Standard HTTP Status Codes
- `200 OK` - Successful GET, PUT operations
- `201 Created` - Successful POST operations  
- `204 No Content` - Successful DELETE operations
- `400 Bad Request` - Invalid request format or parameters
- `401 Unauthorized` - Missing or invalid authentication
- `403 Forbidden` - Valid authentication but insufficient permissions
- `404 Not Found` - Requested resource not found
- `422 Unprocessable Entity` - Validation errors
- `429 Too Many Requests` - Rate limit exceeded
- `500 Internal Server Error` - Server-side errors

### 6.2 Error Response Format
All error responses follow this structure:
```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Request validation failed",
    "details": [
      {
        "field": "email",
        "message": "Invalid email format"
      }
    ],
    "timestamp": "2024-01-15T12:00:00Z"
  }
}
```

## 7. Performance & Scalability

### 7.1 Database Optimization
- **Compound Indexes**: Optimized for common query patterns
- **User-based Sharding**: Potential future sharding by `userId`
- **Connection Pooling**: MongoDB connection pool configuration
- **Query Optimization**: Efficient aggregation pipelines for statistics

### 7.2 Caching Strategy
- **Redis Cache**: Cache user sessions and frequently accessed data
- **Application-level Cache**: Cache user statistics for 5 minutes
- **HTTP Caching**: ETag headers for note list responses

### 7.3 Rate Limiting
- **User Rate Limiting**: 100 requests/minute per authenticated user
- **IP Rate Limiting**: 10 auth requests/minute per IP address
- **Burst Handling**: Allow burst up to 200 requests with token bucket

## 8. Assumptions & Design Decisions

### 8.1 Key Assumptions
1. **Single Language per User**: Users can only select one language at a time
2. **Soft Delete Strategy**: Deleted notes are kept for 7 days for potential recovery
3. **No Note Sharing**: Notes are private to individual users only
4. **Simple Priority System**: No sub-priorities or custom priority levels
5. **No File Attachments**: Notes contain only text descriptions
6. **No Notification System**: As specified in PRD limitations

### 8.2 Design Decisions
1. **JWT over Sessions**: Better scalability for multi-device access
2. **Refresh Token Rotation**: Enhanced security through token rotation
3. **Compound MongoDB Indexes**: Optimized for common query patterns
4. **Separate Statistics Collection**: Denormalized for dashboard performance
5. **RESTful API Design**: Standard REST conventions for ease of use
6. **Pagination Strategy**: Cursor-based pagination for large datasets

### 8.3 Future Considerations
1. **API Versioning**: Support for v2 API through URL path versioning
2. **Multi-language Notes**: Potential expansion to support note translations
3. **Note Categories**: Possible addition of note categorization beyond priorities
4. **Real-time Updates**: WebSocket support for real-time note synchronization
5. **Mobile Optimization**: Specific endpoints optimized for mobile app usage

## 9. API Documentation

### 9.1 OpenAPI Specification
- Complete OpenAPI 3.0 specification will be generated from Spring Boot annotations
- Interactive API documentation available at `/swagger-ui.html`
- JSON schema available at `/v3/api-docs`

### 9.2 SDK Generation
- Client SDKs can be generated from OpenAPI specification
- Support for JavaScript, TypeScript, Flutter/Dart, and other languages
- Automated SDK updates with API changes 