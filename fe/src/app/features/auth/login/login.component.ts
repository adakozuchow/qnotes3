import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from '../../../core/services/auth.service';
import { ErrorDialogComponent } from '../../../shared/components/error-dialog/error-dialog.component';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    template: `
        <div class="login-container">
            <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
                <h2>Login to Qnotes3</h2>
                <div class="form-field">
                    <label for="username">Email</label>
                    <input id="username" type="email" formControlName="username">
                    <div class="error-message" *ngIf="loginForm.get('username')?.errors?.['required'] && loginForm.get('username')?.touched">
                        Email is required
                    </div>
                    <div class="error-message" *ngIf="loginForm.get('username')?.errors?.['email'] && loginForm.get('username')?.touched">
                        Please enter a valid email
                    </div>
                </div>
                <div class="form-field">
                    <label for="password">Password</label>
                    <input id="password" type="password" formControlName="password">
                    <div class="error-message" *ngIf="loginForm.get('password')?.errors?.['required'] && loginForm.get('password')?.touched">
                        Password is required
                    </div>
                </div>
                <button type="submit" [disabled]="!loginForm.valid">Login</button>
                <div class="register-link">
                    <a routerLink="/auth/register">Don't have an account? Register</a>
                </div>
            </form>
        </div>
    `,
    styles: [`
        .login-container {
            max-width: 400px;
            margin: 2rem auto;
            padding: 2rem;
            border: 1px solid #ccc;
            border-radius: 8px;
            background-color: white;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            color: #1976d2;
            margin-bottom: 2rem;
        }
        .form-field {
            margin-bottom: 1.5rem;
        }
        label {
            display: block;
            margin-bottom: 0.5rem;
            color: #333;
        }
        input {
            width: 100%;
            padding: 0.75rem;
            margin-top: 0.25rem;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 1rem;
        }
        input:focus {
            outline: none;
            border-color: #1976d2;
            box-shadow: 0 0 0 2px rgba(25, 118, 210, 0.1);
        }
        .error-message {
            color: #d32f2f;
            font-size: 0.875rem;
            margin-top: 0.25rem;
        }
        button {
            width: 100%;
            padding: 0.75rem;
            background-color: #1976d2;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 1rem;
            margin-top: 1rem;
            transition: background-color 0.2s;
        }
        button:hover {
            background-color: #1565c0;
        }
        button:disabled {
            background-color: #ccc;
            cursor: not-allowed;
        }
        .register-link {
            text-align: center;
            margin-top: 1.5rem;
        }
        .register-link a {
            color: #1976d2;
            text-decoration: none;
        }
        .register-link a:hover {
            text-decoration: underline;
        }
    `]
})
export class LoginComponent {
    loginForm: FormGroup;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router,
        private dialog: MatDialog
    ) {
        this.loginForm = this.fb.group({
            username: ['', [Validators.required, Validators.email]],
            password: ['', Validators.required]
        });
    }

    onSubmit(): void {
        if (this.loginForm.valid) {
            this.authService.login(this.loginForm.value).subscribe({
                next: (response) => {
                    localStorage.setItem('token', response.token);
                    this.router.navigate(['/notes']);
                },
                error: () => {
                    this.dialog.open(ErrorDialogComponent, {
                        data: { message: 'Something went wrong. Contact administrator.' },
                        width: '400px'
                    });
                }
            });
        }
    }
}