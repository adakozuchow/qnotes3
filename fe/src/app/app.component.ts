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
            <!-- Debug info -->
            <div class="debug-info" style="padding: 10px; background: #f0f0f0; margin-bottom: 10px;">
                Current route: {{router.url}}
            </div>

            <header>
                <h1>Qnotes3</h1>
                <nav *ngIf="!authService.isAuthenticated()">
                    <a routerLink="/auth/login">Login</a>
                    <a routerLink="/auth/register">Register</a>
                </nav>
                <button *ngIf="authService.isAuthenticated()" (click)="logout()">Logout</button>
            </header>
            
            <main>
                <!-- Debug info -->
                <div *ngIf="!routerOutletLoaded" style="text-align: center; padding: 20px;">
                    Router outlet not loaded
                </div>
                
                <router-outlet (activate)="onRouterOutletActivate()" (deactivate)="onRouterOutletDeactivate()"></router-outlet>
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
        .debug-info {
            font-family: monospace;
            font-size: 14px;
        }
    `]
})
export class AppComponent {
    routerOutletLoaded = false;

    constructor(
        public authService: AuthService,
        public router: Router
    ) {
        // Debug: Log current route on changes
        this.router.events.subscribe(event => {
            console.log('Router Event:', event);
        });
    }

    onRouterOutletActivate() {
        console.log('Router outlet activated');
        this.routerOutletLoaded = true;
    }

    onRouterOutletDeactivate() {
        console.log('Router outlet deactivated');
        this.routerOutletLoaded = false;
    }

    logout(): void {
        this.authService.logout();
        this.router.navigate(['/auth/login']);
    }
}