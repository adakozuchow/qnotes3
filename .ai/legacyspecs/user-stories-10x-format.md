# User Stories - 10x Format

## Authentication & User Management

### US-001
**Title:** User Registration  
**Description:** As a new user, I want to create an account in the application so that I can start managing my notes.  
**Acceptance Criteria:**
1. Registration form collects:
   - Email (required, must be valid format)
   - Password (min 8 chars, must include numbers and special characters)
   - First name (required)
   - Last name (required)
2. System validates email uniqueness
3. System stores user data in database
4. User receives registration confirmation
5. Password is securely hashed before storage
6. Cannot register with already existing email

### US-002
**Title:** User Login  
**Description:** As a registered user, I want to log into the application so that I can access my notes.  
**Acceptance Criteria:**
1. Login form accepts:
   - Email
   - Password
2. System authenticates credentials
3. System generates JWT token upon successful login
4. Token expires after 24 hours
5. System records last login timestamp
6. Invalid credentials display appropriate error message
7. System prevents brute force attempts

## Note Management

### US-003
**Title:** Create Note  
**Description:** As a logged-in user, I want to add a note with description and priority so that I can track my tasks.  
**Acceptance Criteria:**
1. Note creation form includes:
   - Description field (required, max 300 characters)
   - Priority selector (default: "Na już")
2. System records:
   - Creation timestamp
   - User ID association
3. Note appears in user's list immediately
4. System validates description length
5. System confirms successful creation

### US-004
**Title:** Edit Note  
**Description:** As a logged-in user, I want to edit my existing notes so that I can update their content or status.  
**Acceptance Criteria:**
1. Edit form pre-populated with current:
   - Description
   - Priority
2. System records update timestamp
3. Changes reflect immediately without page refresh
4. Cannot edit other users' notes
5. Maintains note history
6. Updates related statistics if priority changes

### US-005
**Title:** Delete Note  
**Description:** As a logged-in user, I want to delete my notes so that I can remove unnecessary items.  
**Acceptance Criteria:**
1. Deletion requires confirmation
2. Performs soft delete (marks as deleted)
3. Records deletion timestamp
4. Removes from active notes list
5. Cannot delete other users' notes
6. Note marked for cleanup after 7 days
7. Updates user statistics

### US-006
**Title:** View Notes List  
**Description:** As a logged-in user, I want to see my notes list so that I can review all my tasks.  
**Acceptance Criteria:**
1. Displays paginated list showing:
   - Description
   - Priority
   - Creation date
   - Last update date
2. Default sort by priority
3. Configurable page size (max 100)
4. Shows only user's own notes
5. Excludes soft-deleted notes
6. Supports quick priority updates

### US-007
**Title:** Priority-Based Sorting  
**Description:** As a logged-in user, I want to sort notes by priority so that I can focus on important tasks.  
**Acceptance Criteria:**
1. Sort order follows:
   - "Na już"
   - "Na potem"
   - "Na kiedyś"
   - "Zrobione"
2. Maintains sort order after updates
3. Visual indication of current sort
4. Secondary sort by date within priority
5. Persists sort preference

### US-008
**Title:** Date-Based Sorting and Filtering  
**Description:** As a logged-in user, I want to sort and filter notes by date so that I can view time-relevant tasks.  
**Acceptance Criteria:**
1. Supports date filters:
   - Today
   - Past Seven Days
   - All
2. Sort options:
   - Newest first
   - Oldest first
3. Shows filter status
4. Updates count of filtered notes
5. Combines with priority filters

### US-009
**Title:** Priority Dashboard  
**Description:** As a logged-in user, I want to see note counts by priority so that I can understand my task distribution.  
**Acceptance Criteria:**
1. Shows counters for each priority:
   - "NOW" count
   - "LATER" count
   - "SOMEDAY" count
   - "DONE" count
2. Real-time updates on changes
3. Excludes deleted notes
4. Visual indicators for high counts
5. Shows stale notes count (unchanged > 48h)
6. Shows average completion time

### US-010
**Title:** Secure Access  
**Description:** As a user, I want secure access to my notes so that my data remains private.  
**Acceptance Criteria:**
1. All API requests require authentication
2. JWT token validation on each request
3. Automatic token refresh
4. Secure password reset flow
5. Session timeout handling
6. Access audit logging
7. HTTPS enforcement

## Advanced Features

### US-011
**Title:** Stale Notes Tracking  
**Description:** As a logged-in user, I want to identify stale notes so that I can manage neglected tasks.  
**Acceptance Criteria:**
1. Identifies notes unchanged for >48 hours
2. Excludes "Zrobione" status notes
3. Real-time counter updates
4. Notification options
5. Filtering capability

### US-012
**Title:** Completion Analytics  
**Description:** As a logged-in user, I want to see completion statistics so that I can improve my task management.  
**Acceptance Criteria:**
1. Calculates average time to:
   - Mark as "Zrobione"
   - Deletion
2. Shows trends over time
3. Handles edge cases
4. Updates in real-time
5. Exportable statistics

### US-013
**Title:** Automatic Maintenance  
**Description:** As a system administrator, I want automated cleanup so that the system remains optimized.  
**Acceptance Criteria:**
1. Daily cleanup at midnight
2. Soft-deletes completed notes
3. Logs all operations
4. Configurable settings
5. Email notifications
6. Backup before cleanup 