import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { NotesListComponent } from './features/notes/notes-list/notes-list.component';
import { NoteFormComponent } from './features/notes/note-form/note-form.component';
import { StatisticsComponent } from './features/statistics/statistics.component';

export const routes: Routes = [
    { 
        path: 'auth',
        children: [
            { path: 'login', component: LoginComponent },
            { path: 'register', component: RegisterComponent },
            { path: '', redirectTo: 'login', pathMatch: 'full' }
        ]
    },
    {
        path: 'notes',
        canActivate: [AuthGuard],
        children: [
            { path: '', component: NotesListComponent },
            { path: 'new', component: NoteFormComponent },
            { path: 'edit/:id', component: NoteFormComponent }
        ]
    },
    {
        path: 'statistics',
        component: StatisticsComponent,
        canActivate: [AuthGuard]
    },
    { path: '', redirectTo: '/auth/login', pathMatch: 'full' }
];