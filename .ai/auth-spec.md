# Authentication Specification for Qnotes3

## Overview
Qnotes3 implements a stateless authentication system using JSON Web Tokens (JWT) for securing API endpoints. The system provides basic authentication functionality without refresh token support, as per MVP requirements.

## Authentication Flow

### 1. Registration Process
1. **Endpoint**: `POST /api/auth/register`
2. **Request Body**:
   ```json
   {
     "email": "string",
     "password": "string",
     "firstName": "string",
     "lastName": "string"
   }
   ```
3. **Validation Rules**:
   - Email: Valid format, unique in system
   - Password: Minimum 8 characters, must include:
     - Numbers
     - Special characters
   - First Name: Required
   - Last Name: Required

4. **Processing**:
   - Password is hashed using secure hashing algorithm
   - User data stored in MongoDB
   - Email uniqueness verified before creation

5. **Response**:
   - Success (200): Registration confirmation
   - Error (400): Validation errors
   - Error (409): Email already exists

### 2. Login Process
1. **Endpoint**: `POST /api/auth/login`
2. **Request Body**:
   ```json
   {
     "email": "string",
     "password": "string"
   }
   ```

3. **Processing**:
   - Validate credentials against database
   - Generate JWT token on successful authentication
   - Record last login timestamp

4. **Response**:
   ```json
   {
     "token": "JWT_TOKEN",
     "expiresIn": 86400,
     "userId": "string"
   }
   ```

### 3. JWT Implementation

#### Token Structure
1. **Header**:
   ```json
   {
     "alg": "HS256",
     "typ": "JWT"
   }
   ```

2. **Payload**:
   ```json
   {
     "sub": "userId",
     "email": "user@example.com",
     "iat": 1234567890,
     "exp": 1234654290
   }
   ```

#### Token Characteristics
- **Expiration**: 24 hours from issuance
- **Signing Algorithm**: HS256
- **No Refresh Token**: As per MVP requirements

### 4. API Authentication

#### Request Authentication
1. **Header Format**:
   ```
   Authorization: Bearer <JWT_TOKEN>
   ```

2. **Validation Process**:
   - Token presence check
   - Signature verification
   - Expiration check
   - User existence verification

#### Error Handling
1. **HTTP Status Codes**:
   - 401: Unauthorized (missing or invalid token)
   - 403: Forbidden (valid token, insufficient permissions)
   - 400: Bad Request (malformed token)

2. **Error Responses**:
   ```json
   {
     "error": "string",
     "message": "string",
     "timestamp": "string"
   }
   ```

### 5. Security Measures

#### Password Security
1. **Storage**:
   - Passwords never stored in plain text
   - Secure hashing algorithm used
   - Salt automatically handled by hashing implementation

2. **Validation Rules**:
   - Minimum 8 characters
   - Must contain numbers
   - Must contain special characters
   - Common password check

#### Brute Force Protection
1. **Login Attempt Limits**:
   - Maximum 5 failed attempts
   - Temporary lockout after limit reached
   - Lockout duration: 15 minutes

2. **IP-based Rate Limiting**:
   - Maximum 100 requests per minute per IP
   - Applies to authentication endpoints

### 6. Implementation Notes

#### Backend Components
1. **Security Config**:
   - JWT filter configuration
   - CORS settings
   - Authentication entry points

2. **JWT Service**:
   - Token generation
   - Token validation
   - User details extraction

3. **Authentication Controller**:
   - Registration endpoint
   - Login endpoint
   - Token validation endpoint

#### Frontend Integration
1. **Auth Interceptor**:
   - Automatically adds JWT to requests
   - Handles 401/403 responses
   - Redirects to login on token expiration

2. **Auth Guard**:
   - Protects routes requiring authentication
   - Checks token validity
   - Manages route access

3. **Auth Service**:
   - Manages token storage
   - Handles login/logout logic
   - Provides authentication state

### 7. Limitations and Constraints
1. **No Refresh Tokens**:
   - Users must re-login after token expiration
   - No automatic session extension

2. **No Remember Me**:
   - Fixed 24-hour token lifetime
   - No persistent sessions

3. **No OAuth Integration**:
   - Email/password only
   - No social login support

4. **No Multi-factor Authentication**:
   - Basic authentication only
   - No additional security layers

### 8. Future Considerations
1. **Potential Enhancements** (Post-MVP):
   - Refresh token implementation
   - Remember me functionality
   - Password reset flow
   - Email verification
   - OAuth provider integration
   - Multi-factor authentication
