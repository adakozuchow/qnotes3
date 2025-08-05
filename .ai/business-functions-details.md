# Business Functions Specification

## 1. Notes Display Filters

### 1.1 Priority-based Filtering
- **Function**: `filterNotesByPriority`
- **Input**: Priority enum (`NOW`, `LATER`, `SOMEDAY`, `DONE`)
- **Output**: List of filtered notes
- **Description**: Returns notes matching the specified priority level
- **Implementation Notes**:
  - Use existing MongoDB query functionality
  - Return paginated results
  - Sort by creation date (newest first)

### 1.2 Date-based Filtering
- **Function**: `filterNotesByDateRange`
- **Input**: DateRange enum (`TODAY`, `PAST_SEVEN_DAYS`, `ALL`)
- **Output**: List of filtered notes
- **Description**: Returns notes based on their creation date
- **Implementation Notes**:
  - TODAY: Notes created after 00:00 today
  - PAST_SEVEN_DAYS: Notes created in the last 7 days
  - ALL: All notes (paginated)
  - Default sorting: newest first

## 2. Notes Summary Statistics

### 2.1 Stale Notes Counter
- **Function**: `countStaleNotes`
- **Output**: Number of notes
- **Description**: Counts notes that haven't been updated in more than 2 days
- **Implementation Notes**:
  - Compare current timestamp with last update timestamp
  - Exclude notes with priority "DONE"
  - MongoDB aggregation for efficient counting

### 2.2 High Priority Notes Counter
- **Function**: `countHighPriorityNotes`
- **Output**: Number of notes
- **Description**: Counts notes with priority "NOW"
- **Implementation Notes**:
  - Simple count query on priority field
  - Exclude soft-deleted notes

### 2.3 Note Completion Time Analysis
- **Function**: `calculateAverageCompletionTime`
- **Output**: 
  - Average time in hours
  - Separate statistics for:
    - Time to mark as "DONE"
    - Time to deletion (soft or hard)
- **Implementation Notes**:
  - Calculate time difference between creation and priority change
  - Use MongoDB aggregation pipeline
  - Handle edge cases (e.g., notes created and completed on same day)



