import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Statistics } from '../../shared/models/api.models';

@Injectable({
    providedIn: 'root'
})
export class StatisticsService {
    private apiUrl = 'http://localhost:8080/api/notes/statistics';

    constructor(private http: HttpClient) {}

    getStatistics(): Observable<Statistics> {
        return this.http.get<Statistics>(this.apiUrl);
    }
}