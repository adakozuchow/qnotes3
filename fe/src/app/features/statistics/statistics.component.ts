import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StatisticsService } from '../../core/services/statistics.service';
import { Statistics } from '../../shared/models/api.models';

@Component({
    selector: 'app-statistics',
    standalone: true,
    imports: [CommonModule],
    template: `
        <div class="statistics-container">
            <h2>Notes Statistics</h2>
            
            <div class="stats-grid" *ngIf="statistics">
                <div class="stat-card">
                    <div class="stat-title">Stale Notes</div>
                    <div class="stat-value">{{statistics.staleNotesCount}}</div>
                    <div class="stat-description">Notes not updated in more than 2 days</div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-title">High Priority Notes</div>
                    <div class="stat-value">{{statistics.highPriorityNotesCount}}</div>
                    <div class="stat-description">Number of NOW priority notes</div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-title">Average Completion Time</div>
                    <div class="stat-value">{{statistics.averageCompletionTimeHours | number:'1.1-1'}} hours</div>
                    <div class="stat-description">Average time to mark note as DONE</div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-title">Average Deletion Time</div>
                    <div class="stat-value">{{statistics.averageDeletionTimeHours | number:'1.1-1'}} hours</div>
                    <div class="stat-description">Average time to deletion</div>
                </div>
            </div>

            <div class="loading" *ngIf="loading">Loading statistics...</div>
            <div class="error" *ngIf="error">{{error}}</div>
        </div>
    `,
    styles: [`
        .statistics-container {
            padding: 2rem;
            max-width: 1200px;
            margin: 0 auto;
        }

        h2 {
            color: #333;
            margin-bottom: 2rem;
            text-align: center;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1.5rem;
        }

        .stat-card {
            background: white;
            border-radius: 8px;
            padding: 1.5rem;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            text-align: center;
            transition: transform 0.2s;
        }

        .stat-card:hover {
            transform: translateY(-2px);
        }

        .stat-title {
            color: #666;
            font-size: 1rem;
            margin-bottom: 0.5rem;
        }

        .stat-value {
            color: #1976d2;
            font-size: 2rem;
            font-weight: bold;
            margin-bottom: 0.5rem;
        }

        .stat-description {
            color: #888;
            font-size: 0.875rem;
            line-height: 1.4;
        }

        .loading {
            text-align: center;
            color: #666;
            padding: 2rem;
        }

        .error {
            text-align: center;
            color: #d32f2f;
            padding: 1rem;
            background: #ffebee;
            border-radius: 4px;
            margin-top: 1rem;
        }
    `]
})
export class StatisticsComponent implements OnInit {
    statistics: Statistics | null = null;
    loading = false;
    error: string | null = null;

    constructor(private statisticsService: StatisticsService) {}

    ngOnInit(): void {
        this.loadStatistics();
    }

    loadStatistics(): void {
        this.loading = true;
        this.error = null;

        this.statisticsService.getStatistics().subscribe({
            next: (stats) => {
                this.statistics = stats;
                this.loading = false;
            },
            error: (err) => {
                this.error = 'Failed to load statistics. Please try again later.';
                this.loading = false;
                console.error('Error loading statistics:', err);
            }
        });
    }
}