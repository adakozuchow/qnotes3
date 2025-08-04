# MongoDB Database Schema - Qnotes MVP

## 1. Collections with document fields and data types

### Collection: `users`
```javascript
{
  _id: ObjectId,                    // MongoDB auto-generated ID
  email: String,                    // Unique, required, max 254 characters
  passwordHash: String,             // BCrypt hashed password, required
  createdAt: Date,                  // Auto-generated timestamp
  updatedAt: Date                   // Auto-updated timestamp
}
```

**Constraints:**
- `email`: unique, required, valid email format, max 254 characters
- `passwordHash`: required, min 60 characters (BCrypt hash length)

### Collection: `notes`
```javascript
{
  _id: ObjectId,                    // MongoDB auto-generated ID
  userId: ObjectId,                 // Reference to users._id, required
  description: String,              // Note content, required, max 300 characters
  priority: String,                 // Priority level, required
  createdAt: Date,                  // Auto-generated timestamp
  updatedAt: Date,                  // Auto-updated timestamp
  deleted: Boolean,                 // Soft delete flag, default false
  deletedAt: Date,                  // Timestamp when soft deleted
  completedAt: Date                 // Timestamp when marked as "DONE" for TTL
}
```

**Constraints:**
- `userId`: required, must reference existing user
- `description`: required, min 1 character, max 300 characters
- `priority`: required, enum ["NOW", "LATER", "SOMEDAY", "DONE"]
- `deleted`: default false
- `deletedAt`: set only when deleted = true
- `completedAt`: set only when priority = "DONE"

### Collection: `refreshTokens`
```javascript
{
  _id: ObjectId,                    // MongoDB auto-generated ID
  token: String,                    // JWT refresh token, required, unique
  userId: ObjectId,                 // Reference to users._id, required
  expiresAt: Date,                  // Token expiration date, required
  createdAt: Date,                  // Auto-generated timestamp
  isRevoked: Boolean                // Token revocation status, default false
}
```

**Constraints:**
- `token`: required, unique, min 100 characters
- `userId`: required, must reference existing user
- `expiresAt`: required, must be future date
- `isRevoked`: default false

### Collection: `userStats` (Dashboard counters)
```javascript
{
  _id: ObjectId,                    // MongoDB auto-generated ID
  userId: ObjectId,                 // Reference to users._id, required, unique
  nowCount: Number,                 // Count of "NOW" notes, default 0
  laterCount: Number,               // Count of "LATER" notes, default 0
  somedayCount: Number,             // Count of "SOMEDAY" notes, default 0
  doneCount: Number,                // Count of "DONE" notes, default 0
  updatedAt: Date                   // Last update timestamp
}
```

**Constraints:**
- `userId`: required, unique, must reference existing user
- All count fields: default 0, min 0

## 2. Indexes

### Collection `users`
```javascript
// Unique index on email for fast login lookup and uniqueness
db.users.createIndex({ "email": 1 }, { unique: true })
```

### Collection `notes`
```javascript
// Primary compound index for user's notes filtering and sorting
db.notes.createIndex({ "userId": 1, "deleted": 1, "priority": 1, "createdAt": -1 })

// Index for soft delete and TTL operations
db.notes.createIndex({ "deleted": 1, "deletedAt": 1 })
```

### Collection `refreshTokens`
```javascript
// Indexes for token management and cleanup
db.refreshTokens.createIndex({ "token": 1 }, { unique: true })
db.refreshTokens.createIndex({ "userId": 1 })
db.refreshTokens.createIndex({ "expiresAt": 1 }, { expireAfterSeconds: 0 })
```

### Collection `userStats`
```javascript
// Unique index on userId for fast lookups
db.userStats.createIndex({ "userId": 1 }, { unique: true })
```

## 3. Validators

### Validator for `users` collection
```javascript
db.createCollection("users", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["email", "passwordHash", "createdAt", "updatedAt"],
      properties: {
        email: {
          bsonType: "string",
          pattern: "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
          maxLength: 254,
          description: "must be a valid email address"
        },
        passwordHash: {
          bsonType: "string",
          minLength: 60,
          maxLength: 60,
          description: "must be a BCrypt hash"
        },
        createdAt: {
          bsonType: "date",
          description: "must be a date"
        },
        updatedAt: {
          bsonType: "date",
          description: "must be a date"
        }
      }
    }
  }
})
```

### Validator for `notes` collection
```javascript
db.createCollection("notes", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["userId", "description", "priority", "createdAt", "updatedAt"],
      properties: {
        userId: {
          bsonType: "objectId",
          description: "must be a valid user ID"
        },
        description: {
          bsonType: "string",
          minLength: 1,
          maxLength: 300,
          description: "must be between 1 and 300 characters"
        },
        priority: {
          bsonType: "string",
          enum: ["NOW", "LATER", "SOMEDAY", "DONE"],
          description: "must be a valid priority level"
        },
        createdAt: {
          bsonType: "date",
          description: "must be a date"
        },
        updatedAt: {
          bsonType: "date",
          description: "must be a date"
        },
        deleted: {
          bsonType: "bool",
          description: "must be a boolean"
        },
        deletedAt: {
          bsonType: ["date", "null"],
          description: "must be a date or null"
        },
        completedAt: {
          bsonType: ["date", "null"],
          description: "must be a date or null"
        }
      }
    }
  }
})
```

## 4. Additional Notes and Design Decisions

### TTL (Time To Live) Strategy
- **Soft-deleted notes**: Auto-remove after 7 days from `deletedAt`
- **Completed notes**: Auto-remove after 7 days from `completedAt` (when priority = "DONE")
- **Refresh tokens**: Auto-remove after expiration (`expiresAt`)

### Dashboard and Counters
- Separate `userStats` collection for efficient counter access
- Update counters on every CRUD operation on notes
- Atomic operations on single documents

### Security
- Passwords hashed using BCrypt (60 characters)
- Refresh tokens with revocation mechanism
- MongoDB schema-level validation
- Unique indexes for critical fields (email, token)

### Performance
- Compound indexes optimized for application queries
- Index on `userId` for fast note filtering
- TTL indexes for automatic data lifecycle management
- Separate `completedAt` field for efficient TTL on completed tasks

### Scalability
- Document structure optimized for atomic operations
- No complex relationships requiring multi-document transactions
- Future sharding possible by `userId`
- Counter denormalization for better dashboard performance

### Priority Mapping
Priorities stored as strings according to PRD requirements:
- "NOW" (default)
- "LATER"
- "SOMEDAY"
- "DONE" 