# User Stories for Qnotes3

## 1. Authentication & User Management

### US-1.1: User Registration
**As a** new user  
**I want to** register in the application  
**So that** I can start managing my notes  

**Acceptance Criteria:**
1. Registration form collects:
   - Email (required, must be valid format)
   - Password (min 8 chars, must include numbers and special characters)
   - First name (required)
   - Last name (required)
2. System validates email uniqueness
3. System stores user data securely (password hashed)
4. User receives registration confirmation
5. Cannot register with already existing email

### US-1.2: User Login
**As a** registered user  
**I want to** log into the application  
**So that** I can access my notes  

**Acceptance Criteria:**
1. Can login with valid email and password
2. System generates JWT token upon successful login
3. Token expires after 24 hours (no refresh token support)
4. Cannot login with invalid credentials
5. System prevents brute force attempts
6. Last login time is recorded

## 2. Note Management - Core Operations

### US-2.1: Create Note
**As a** logged-in user  
**I want to** create a new note  
**So that** I can keep track of my tasks  

**Acceptance Criteria:**
1. Note creation form includes:
   - Description field (required, max 300 characters)
   - Priority selector (default: "NOW")
2. System records:
   - Creation timestamp
   - User ID association
3. Note appears in user's list immediately
4. System validates description length
5. System confirms successful creation

### US-2.2: View Notes List
**As a** logged-in user  
**I want to** view my notes  
**So that** I can see all my tasks  

**Acceptance Criteria:**
1. Displays paginated list showing:
   - Description
   - Priority
   - Creation date
   - Last update date
2. Default sort by priority
3. Configurable page size (max 100)
4. Only see own notes
5. Soft-deleted notes are not shown

### US-2.3: Update Note
**As a** logged-in user  
**I want to** update my notes  
**So that** I can modify task details or status  

**Acceptance Criteria:**
1. Can update:
   - Description (max 300 characters)
   - Priority level
2. System records update timestamp
3. Cannot update other users' notes
4. Validation rules still apply
5. Changes reflect immediately
6. Updates related statistics if priority changes

### US-2.4: Delete Note
**As a** logged-in user  
**I want to** delete my notes  
**So that** I can remove completed or unnecessary tasks  

**Acceptance Criteria:**
1. Deletion requires confirmation
2. Performs soft delete (marks as deleted)
3. Records deletion timestamp
4. Removes from active notes list
5. Cannot delete other users' notes
6. MongoDB TTL index handles cleanup

## 3. Note Management - Filtering & Sorting

### US-3.1: Priority-Based Filtering
**As a** logged-in user  
**I want to** filter notes by priority  
**So that** I can focus on specific priority levels  

**Acceptance Criteria:**
1. Can filter by priority levels:
   - NOW
   - LATER
   - SOMEDAY
   - DONE
2. Results are paginated
3. Sort by creation date (newest first)
4. Only active notes shown
5. Empty result handled gracefully

### US-3.2: Date-Based Filtering
**As a** logged-in user  
**I want to** filter notes by date range  
**So that** I can view notes from specific time periods  

**Acceptance Criteria:**
1. Supports date filters:
   - Today (after 00:00)
   - Past Seven Days
   - All
2. Results are paginated
3. Sort by newest first
4. Shows filter status
5. Shows count of filtered notes

## 4. Statistics and Analytics

### US-4.1: View Stale Notes Counter
**As a** logged-in user  
**I want to** see notes unchanged for over 2 days  
**So that** I can identify neglected tasks  

**Acceptance Criteria:**
1. Shows count of notes not updated in 48+ hours
2. Excludes notes with priority "DONE"
3. Updates in real-time
4. Only counts user's own notes
5. Excludes deleted notes
6. Uses MongoDB aggregation for efficiency

### US-4.2: View High Priority Counter
**As a** logged-in user  
**I want to** see count of high priority notes  
**So that** I can manage urgent tasks  

**Acceptance Criteria:**
1. Shows count of "NOW" priority notes
2. Excludes deleted notes
3. Updates when priority changes
4. Only counts user's notes
5. Uses simple MongoDB count query

### US-4.3: View Completion Analytics
**As a** logged-in user  
**I want to** see average completion time statistics  
**So that** I can understand my task completion patterns  

**Acceptance Criteria:**
1. Shows average time to:
   - Mark as "DONE"
   - Deletion
2. Calculates from creation time
3. Uses MongoDB aggregation pipeline
4. Handles edge cases (same day completion)
5. Updates with new data

## 5. System Maintenance

### US-5.1: Automatic Note Cleanup
**As a** system  
**I want to** automatically clean up eligible notes  
**So that** the database stays optimized  

**Acceptance Criteria:**
1. MongoDB TTL index configured for cleanup
2. Removes soft-deleted notes automatically
3. No manual cleanup process required
4. Configurable through MongoDB settings
5. No impact on user statistics
