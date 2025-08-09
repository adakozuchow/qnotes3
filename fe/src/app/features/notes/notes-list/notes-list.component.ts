import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotesService } from '../../../core/services/notes.service';
import { Note, NotePriority } from '../../../shared/models/api.models';

type SortOption = 'date-asc' | 'date-desc' | 'priority';

@Component({
    selector: 'app-notes-list',
    standalone: true,
    imports: [CommonModule, FormsModule],
    template: `
        <div class="notes-container">
            <div class="header">
                <h2>My Notes</h2>
                <button class="new-note-btn" (click)="createNewNote()">New Note</button>
            </div>

            <div class="filters">
                <div class="filter-group">
                    <label for="sortBy">Sort by:</label>
                    <select id="sortBy" [(ngModel)]="currentSort" (change)="applySorting()">
                        <option value="date-desc">Date (Newest first)</option>
                        <option value="date-asc">Date (Oldest first)</option>
                        <option value="priority">Priority</option>
                    </select>
                </div>

                <div class="filter-group">
                    <label for="priorityFilter">Filter by priority:</label>
                    <select id="priorityFilter" [(ngModel)]="selectedPriority" (change)="applyFilters()">
                        <option value="">All</option>
                        <option *ngFor="let priority of priorities" [value]="priority">
                            {{priority}}
                        </option>
                    </select>
                </div>
            </div>
            
            <div class="notes-grid">
                <div *ngFor="let note of filteredNotes" class="note-card">
                    <h3>{{note.title}}</h3>
                    <p class="content">{{note.content}}</p>
                    <div class="note-footer">
                        <span class="priority" [class]="note.priority.toLowerCase()">
                            {{note.priority}}
                        </span>
                        <div class="actions">
                            <button class="edit-btn" (click)="editNote(note)">Edit</button>
                            <button class="delete-btn" (click)="deleteNote(note.id)">Delete</button>
                        </div>
                    </div>
                    <div class="note-date">
                        Created: {{formatDate(note.createdAt)}}
                    </div>
                </div>
            </div>

            <div *ngIf="filteredNotes.length === 0" class="no-notes">
                No notes found matching the selected filters.
            </div>
            
            <div class="pagination" *ngIf="totalPages > 1">
                <button [disabled]="currentPage === 0" 
                        (click)="loadPage(currentPage - 1)">
                    Previous
                </button>
                <span>Page {{currentPage + 1}} of {{totalPages}}</span>
                <button [disabled]="currentPage >= totalPages - 1" 
                        (click)="loadPage(currentPage + 1)">
                    Next
                </button>
            </div>
        </div>
    `,
    styles: [`
        .notes-container {
            padding: 2rem;
            max-width: 1200px;
            margin: 0 auto;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
        }
        .filters {
            display: flex;
            gap: 1rem;
            margin-bottom: 2rem;
            padding: 1rem;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }
        .filter-group {
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        .filter-group label {
            color: #666;
            font-size: 0.9rem;
        }
        .filter-group select {
            padding: 0.5rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            background-color: white;
            color: #333;
            font-size: 0.9rem;
        }
        .new-note-btn {
            padding: 0.5rem 1rem;
            background-color: #1976d2;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .notes-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 1.5rem;
        }
        .note-card {
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 1rem;
            background-color: white;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            position: relative;
        }
        .note-card h3 {
            margin: 0 0 1rem 0;
            color: #333;
        }
        .content {
            color: #666;
            margin-bottom: 1rem;
            max-height: 100px;
            overflow: hidden;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
        }
        .note-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 0.5rem;
        }
        .priority {
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            font-size: 0.875rem;
            font-weight: 500;
        }
        .priority.now { 
            background-color: #ffebee; 
            color: #d32f2f;
        }
        .priority.later { 
            background-color: #fff3e0; 
            color: #f57c00;
        }
        .priority.someday { 
            background-color: #e8f5e9; 
            color: #388e3c;
        }
        .priority.done { 
            background-color: #e3f2fd; 
            color: #1976d2;
        }
        .actions {
            display: flex;
            gap: 0.5rem;
        }
        .actions button {
            padding: 0.25rem 0.5rem;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.875rem;
        }
        .edit-btn {
            background-color: #e3f2fd;
            color: #1976d2;
        }
        .delete-btn {
            background-color: #ffebee;
            color: #d32f2f;
        }
        .pagination {
            margin-top: 2rem;
            display: flex;
            justify-content: center;
            gap: 1rem;
            align-items: center;
        }
        .pagination button {
            padding: 0.5rem 1rem;
            border: 1px solid #1976d2;
            background-color: white;
            color: #1976d2;
            border-radius: 4px;
            cursor: pointer;
        }
        .pagination button:disabled {
            border-color: #ccc;
            color: #ccc;
            cursor: not-allowed;
        }
        .note-date {
            font-size: 0.75rem;
            color: #999;
            text-align: right;
            margin-top: 0.5rem;
            border-top: 1px solid #eee;
            padding-top: 0.5rem;
        }
        .no-notes {
            text-align: center;
            padding: 2rem;
            color: #666;
            background-color: white;
            border-radius: 8px;
            margin-top: 1rem;
        }
    `]
})
export class NotesListComponent implements OnInit {
    notes: Note[] = [];
    filteredNotes: Note[] = [];
    currentPage = 0;
    totalPages = 0;
    currentSort: SortOption = 'date-desc';
    selectedPriority: string = '';
    priorities = Object.values(NotePriority);

    constructor(
        private notesService: NotesService,
        private router: Router
    ) {}

    ngOnInit(): void {
        this.loadPage(0);
    }

    loadPage(page: number): void {
        this.notesService.getNotes(page).subscribe({
            next: (response) => {
                this.notes = response.notes;
                this.currentPage = response.currentPage;
                this.totalPages = response.totalPages;
                this.applyFiltersAndSort();
            },
            error: (error) => console.error('Failed to load notes:', error)
        });
    }

    createNewNote(): void {
        this.router.navigate(['/notes/new']);
    }

    editNote(note: Note): void {
        this.router.navigate(['/notes/edit', note.id]);
    }

    deleteNote(id: string): void {
        if (confirm('Are you sure you want to delete this note?')) {
            this.notesService.deleteNote(id).subscribe({
                next: () => this.loadPage(this.currentPage),
                error: (error) => console.error('Failed to delete note:', error)
            });
        }
    }

    formatDate(date: string): string {
        return new Date(date).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    applySorting(): void {
        this.applyFiltersAndSort();
    }

    applyFilters(): void {
        this.applyFiltersAndSort();
    }

    private applyFiltersAndSort(): void {
        // First apply filters
        this.filteredNotes = this.notes.filter(note => {
            if (!this.selectedPriority) return true;
            return note.priority === this.selectedPriority;
        });

        // Then apply sorting
        this.filteredNotes.sort((a, b) => {
            switch (this.currentSort) {
                case 'date-asc':
                    return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime();
                case 'date-desc':
                    return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
                case 'priority':
                    const priorityOrder = {
                        [NotePriority.NOW]: 0,
                        [NotePriority.LATER]: 1,
                        [NotePriority.SOMEDAY]: 2,
                        [NotePriority.DONE]: 3
                    };
                    return priorityOrder[a.priority] - priorityOrder[b.priority];
                default:
                    return 0;
            }
        });
    }
}