import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    template: `
        <div class="register-container">
            <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
                <h2>Register</h2>
                <div class="form-field">
                    <label for="username">Email</label>
                    <input id="username" type="email" formControlName="username">
                    <div *ngIf="registerForm.get('username')?.errors?.['required'] && registerForm.get('username')?.touched">
                        Email is required
                    </div>
                    <div *ngIf="registerForm.get('username')?.errors?.['email'] && registerForm.get('username')?.touched">
                        Please enter a valid email
                    </div>
                </div>
                <div class="form-field">
                    <label for="password">Password</label>
                    <input id="password" type="password" formControlName="password">
                    <div *ngIf="registerForm.get('password')?.errors?.['required'] && registerForm.get('password')?.touched">
                        Password is required
                    </div>
                    <div *ngIf="registerForm.get('password')?.errors?.['minlength'] && registerForm.get('password')?.touched">
                        Password must be at least 8 characters
                    </div>
                </div>
                <button type="submit" [disabled]="!registerForm.valid">Register</button>
                <div class="login-link">
                    <a routerLink="/auth/login">Already have an account? Login</a>
                </div>
            </form>
        </div>
    `,
    styles: [`
        .register-container {
            max-width: 400px;
            margin: 2rem auto;
            padding: 2rem;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .form-field {
            margin-bottom: 1rem;
        }
        label {
            display: block;
            margin-bottom: 0.5rem;
        }
        input {
            width: 100%;
            padding: 0.5rem;
            margin-top: 0.25rem;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        button {
            width: 100%;
            padding: 0.75rem;
            background-color: #1976d2;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 1rem;
        }
        button:disabled {
            background-color: #ccc;
        }
        .login-link {
            text-align: center;
            margin-top: 1rem;
        }
        .login-link a {
            color: #1976d2;
            text-decoration: none;
        }
    `]
})
export class RegisterComponent {
    registerForm: FormGroup;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) {
        this.registerForm = this.fb.group({
            username: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(8)]]
        });
    }

    onSubmit(): void {
        if (this.registerForm.valid) {
            this.authService.register(this.registerForm.value).subscribe({
                next: (response) => {
                    localStorage.setItem('token', response.token);
                    this.router.navigate(['/notes']);
                },
                error: (error) => {
                    console.error('Registration failed:', error);
                }
            });
        }
    }
}