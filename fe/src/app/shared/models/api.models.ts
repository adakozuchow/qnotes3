export interface LoginRequest {
    username: string;
    password: string;
}

export interface RegisterRequest {
    username: string;
    password: string;
}

export interface AuthResponse {
    token: string;
}

export enum NotePriority {
    NOW = 'NOW',
    LATER = 'LATER',
    SOMEDAY = 'SOMEDAY',
    DONE = 'DONE'
}

export interface Note {
    id: string;
    title: string;
    content: string;
    priority: NotePriority;
    createdAt: string;
    updatedAt: string;
    deletedAt: string | null;
}

export interface NoteRequest {
    title: string;
    content: string;
    priority: NotePriority;
}

export interface NotesResponse {
    notes: Note[];
    totalPages: number;
    currentPage: number;
}

export interface Statistics {
    staleNotesCount: number;
    highPriorityNotesCount: number;
    averageCompletionTimeHours: number;
    averageDeletionTimeHours: number;
}