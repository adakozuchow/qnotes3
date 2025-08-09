import { Component } from '@angular/core';
import { Router, RouterOutlet, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './core/services/auth.service';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [CommonModule, RouterOutlet, RouterLink],
    template: `
        <div class="app-container">
            <header>
                <h1>Qnotes3</h1>
                <nav>
                    @if (!authService.isAuthenticated()) {
                        <a routerLink="/auth/login">Login</a>
                        <a routerLink="/auth/register">Register</a>
                    } @else {
                        <a routerLink="/notes">Notes</a>
                        <a routerLink="/statistics">Statistics</a>
                        <button (click)="logout()">Logout</button>
                    }
                </nav>
            </header>
            
            <main>
                <router-outlet></router-outlet>
            </main>
        </div>
    `,
    styles: [`
        .app-container {
            min-height: 100vh;
            background-color: #f5f5f5;
        }
        header {
            background-color: white;
            padding: 1rem 2rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            margin: 0;
            color: #1976d2;
        }
        nav {
            display: flex;
            gap: 1rem;
            align-items: center;
        }
        nav a {
            color: #1976d2;
            text-decoration: none;
            padding: 0.5rem 1rem;
            border-radius: 4px;
        }
        nav a:hover {
            background-color: #e3f2fd;
        }
        button {
            padding: 0.5rem 1rem;
            background-color: transparent;
            border: 1px solid #1976d2;
            color: #1976d2;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #1976d2;
            color: white;
        }
        main {
            padding: 1rem;
            max-width: 1200px;
            margin: 0 auto;
        }
    `]
})
export class AppComponent {
    constructor(
        public authService: AuthService,
        private router: Router
    ) {}

    logout(): void {
        this.authService.logout();
        this.router.navigate(['/auth/login']);
    }
}