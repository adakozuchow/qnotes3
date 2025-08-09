import { Routes } from '@angular/router';
import { NotesListComponent } from './notes-list/notes-list.component';
import { NoteFormComponent } from './note-form/note-form.component';

export const NOTES_ROUTES: Routes = [
    { path: '', component: NotesListComponent },
    { path: 'new', component: NoteFormComponent },
    { path: 'edit/:id', component: NoteFormComponent }
];