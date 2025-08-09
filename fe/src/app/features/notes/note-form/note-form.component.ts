import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NotesService } from '../../../core/services/notes.service';
import { NotePriority } from '../../../shared/models/api.models';

@Component({
    selector: 'app-note-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    template: `
        <div class="form-container">
            <h2>{{isEditMode ? 'Edit Note' : 'Create Note'}}</h2>
            <form [formGroup]="noteForm" (ngSubmit)="onSubmit()">
                <div class="form-field">
                    <label for="title">Title</label>
                    <input id="title" type="text" formControlName="title">
                    <div *ngIf="noteForm.get('title')?.errors?.['required'] && noteForm.get('title')?.touched">
                        Title is required
                    </div>
                </div>
                
                <div class="form-field">
                    <label for="content">Content</label>
                    <textarea id="content" formControlName="content" rows="6"></textarea>
                    <div *ngIf="noteForm.get('content')?.errors?.['required'] && noteForm.get('content')?.touched">
                        Content is required
                    </div>
                </div>
                
                <div class="form-field">
                    <label for="priority">Priority</label>
                    <select id="priority" formControlName="priority">
                        <option *ngFor="let priority of priorities" [value]="priority">
                            {{priority}}
                        </option>
                    </select>
                </div>
                
                <div class="form-actions">
                    <button type="button" class="cancel-btn" (click)="cancel()">Cancel</button>
                    <button type="submit" [disabled]="!noteForm.valid">
                        {{isEditMode ? 'Update' : 'Create'}}
                    </button>
                </div>
            </form>
        </div>
    `,
    styles: [`
        .form-container {
            max-width: 600px;
            margin: 2rem auto;
            padding: 2rem;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            background-color: white;
        }
        .form-field {
            margin-bottom: 1.5rem;
        }
        label {
            display: block;
            margin-bottom: 0.5rem;
            color: #333;
            font-weight: 500;
        }
        input, textarea, select {
            width: 100%;
            padding: 0.5rem;
            border: 1px solid #e0e0e0;
            border-radius: 4px;
            font-size: 1rem;
        }
        textarea {
            resize: vertical;
        }
        .form-actions {
            display: flex;
            gap: 1rem;
            justify-content: flex-end;
            margin-top: 2rem;
        }
        button {
            padding: 0.5rem 1rem;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 1rem;
        }
        button[type="submit"] {
            background-color: #1976d2;
            color: white;
        }
        button[type="submit"]:disabled {
            background-color: #ccc;
            cursor: not-allowed;
        }
        .cancel-btn {
            background-color: #f5f5f5;
            color: #333;
        }
        .error-message {
            color: #d32f2f;
            font-size: 0.875rem;
            margin-top: 0.25rem;
        }
    `]
})
export class NoteFormComponent implements OnInit {
    noteForm: FormGroup;
    isEditMode = false;
    priorities = Object.values(NotePriority);

    constructor(
        private fb: FormBuilder,
        private notesService: NotesService,
        private router: Router,
        private route: ActivatedRoute
    ) {
        this.noteForm = this.fb.group({
            title: ['', Validators.required],
            content: ['', Validators.required],
            priority: [NotePriority.LATER, Validators.required]
        });
    }

    ngOnInit(): void {
        const noteId = this.route.snapshot.paramMap.get('id');
        if (noteId) {
            this.isEditMode = true;
            this.notesService.getNote(noteId).subscribe({
                next: (note) => {
                    this.noteForm.patchValue({
                        title: note.title,
                        content: note.content,
                        priority: note.priority
                    });
                },
                error: (error) => {
                    console.error('Failed to load note:', error);
                    this.router.navigate(['/notes']);
                }
            });
        }
    }

    onSubmit(): void {
        if (this.noteForm.valid) {
            const noteId = this.route.snapshot.paramMap.get('id');
            const operation = this.isEditMode
                ? this.notesService.updateNote(noteId!, this.noteForm.value)
                : this.notesService.createNote(this.noteForm.value);

            operation.subscribe({
                next: () => this.router.navigate(['/notes']),
                error: (error) => console.error('Failed to save note:', error)
            });
        }
    }

    cancel(): void {
        this.router.navigate(['/notes']);
    }
}