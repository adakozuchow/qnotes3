# User Stories for Testing

## 1. Authentication & User Management

### US-1.1: User Registration
**As a** new user  
**I want to** register in the application  
**So that** I can start managing my notes  

**Acceptance Criteria:**
- Can register with valid email and password
- Password must be at least 8 characters with numbers and special characters
- Receive confirmation of successful registration
- Cannot register with already existing email
- First name and last name are required

### US-1.2: User Login
**As a** registered user  
**I want to** log into the application  
**So that** I can access my notes  

**Acceptance Criteria:**
- Can login with valid credentials
- Receive JWT token for authentication
- Token expires after 24 hours
- Cannot login with invalid credentials
- Last login time is recorded

## 2. Note Management - Basic Operations

### US-2.1: Create Note
**As a** logged-in user  
**I want to** create a new note  
**So that** I can keep track of my tasks  

**Acceptance Criteria:**
- Can create note with description
- Can set priority level (defaults to "Na już")
- Description limited to 300 characters
- Note is associated with current user
- Creation time is recorded

### US-2.2: View Notes List
**As a** logged-in user  
**I want to** view my notes  
**So that** I can see all my tasks  

**Acceptance Criteria:**
- See paginated list of notes
- Default sort by priority
- Can change page size (max 100)
- Only see own notes
- Soft-deleted notes are not shown

### US-2.3: Update Note
**As a** logged-in user  
**I want to** update my notes  
**So that** I can modify task details or status  

**Acceptance Criteria:**
- Can update description and priority
- Update time is recorded
- Cannot update other users' notes
- Validation rules still apply
- Status changes update statistics

### US-2.4: Delete Note
**As a** logged-in user  
**I want to** delete my notes  
**So that** I can remove completed or unnecessary tasks  

**Acceptance Criteria:**
- Can soft-delete own notes
- Deleted notes not visible in main list
- Cannot delete other users' notes
- Deletion time is recorded
- Note is marked for cleanup after 7 days

## 3. Note Management - Advanced Filtering

### US-3.1: Filter by Priority
**As a** logged-in user  
**I want to** filter notes by priority  
**So that** I can focus on specific priority levels  

**Acceptance Criteria:**
- Can filter by any priority level
- Results are paginated
- Only active notes shown
- Default sort by creation date
- Empty result handled gracefully

### US-3.2: Filter by Date Range
**As a** logged-in user  
**I want to** filter notes by date range  
**So that** I can view notes from specific time periods  

**Acceptance Criteria:**
- Can filter by: Today, Past Seven Days, All
- Results are paginated
- Dates based on creation time
- Sort by newest first
- Shows correct count of filtered notes

## 4. Statistics and Analytics

### US-4.1: View Stale Notes
**As a** logged-in user  
**I want to** see notes unchanged for over 2 days  
**So that** I can identify neglected tasks  

**Acceptance Criteria:**
- Shows count of notes not updated in 48+ hours
- Excludes completed notes
- Updates in real-time
- Only counts user's own notes
- Excludes deleted notes

### US-4.2: View High Priority Statistics
**As a** logged-in user  
**I want to** see count of high priority notes  
**So that** I can manage urgent tasks  

**Acceptance Criteria:**
- Shows count of "Na już" priority notes
- Excludes deleted notes
- Updates when priority changes
- Only counts user's notes
- Real-time counter updates

### US-4.3: View Completion Analytics
**As a** logged-in user  
**I want to** see average completion time statistics  
**So that** I can understand my task completion patterns  

**Acceptance Criteria:**
- Shows average time to mark as "Zrobione"
- Shows average time to deletion
- Calculates from creation time
- Handles edge cases (same day completion)
- Updates with new data

## 5. Automatic Maintenance

### US-5.1: Auto-cleanup of Completed Notes
**As a** system administrator  
**I want** completed notes to be automatically cleaned up  
**So that** the database stays optimized  

**Acceptance Criteria:**
- Runs daily at midnight
- Soft-deletes notes marked as "Zrobione"
- Logs cleanup operations
- Configurable through properties
- Updates user statistics after cleanup 