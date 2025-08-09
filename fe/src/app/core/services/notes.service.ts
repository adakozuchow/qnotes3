import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Note, NoteRequest, NotesResponse } from '../../shared/models/api.models';

@Injectable({
    providedIn: 'root'
})
export class NotesService {
    private apiUrl = 'http://localhost:8080/api/notes';

    constructor(private http: HttpClient) {}

    getNotes(page: number = 0): Observable<NotesResponse> {
        const params = new HttpParams().set('page', page.toString());
        return this.http.get<NotesResponse>(this.apiUrl, { params });
    }

    getNote(id: string): Observable<Note> {
        return this.http.get<Note>(`${this.apiUrl}/${id}`);
    }

    createNote(note: NoteRequest): Observable<Note> {
        return this.http.post<Note>(this.apiUrl, note);
    }

    updateNote(id: string, note: NoteRequest): Observable<Note> {
        return this.http.put<Note>(`${this.apiUrl}/${id}`, note);
    }

    deleteNote(id: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}