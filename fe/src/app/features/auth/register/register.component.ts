import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from '../../../core/services/auth.service';
import { ErrorDialogComponent } from '../../../shared/components/error-dialog/error-dialog.component';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    template: `
        <div class="register-container">
            <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
                <h2>Register for Qnotes3</h2>
                <div class="form-field">
                    <label for="username">Email</label>
                    <input id="username" type="email" formControlName="username">
                    <div class="error-message" *ngIf="registerForm.get('username')?.errors?.['required'] && registerForm.get('username')?.touched">
                        Email is required
                    </div>
                    <div class="error-message" *ngIf="registerForm.get('username')?.errors?.['email'] && registerForm.get('username')?.touched">
                        Please enter a valid email
                    </div>
                </div>
                <div class="form-field">
                    <label for="password">Password</label>
                    <input id="password" type="password" formControlName="password">
                    <div class="error-message" *ngIf="registerForm.get('password')?.errors?.['required'] && registerForm.get('password')?.touched">
                        Password is required
                    </div>
                    <div class="error-message" *ngIf="registerForm.get('password')?.errors?.['minlength'] && registerForm.get('password')?.touched">
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
        .login-link {
            text-align: center;
            margin-top: 1.5rem;
        }
        .login-link a {
            color: #1976d2;
            text-decoration: none;
        }
        .login-link a:hover {
            text-decoration: underline;
        }
    `]
})
export class RegisterComponent {
    registerForm: FormGroup;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router,
        private dialog: MatDialog
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