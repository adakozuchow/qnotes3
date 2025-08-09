import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
    selector: 'app-error-dialog',
    standalone: true,
    imports: [CommonModule, MatDialogModule, MatButtonModule],
    template: `
        <h2 mat-dialog-title>Error</h2>
        <mat-dialog-content>
            <p>{{data.message}}</p>
        </mat-dialog-content>
        <mat-dialog-actions align="end">
            <button mat-button (click)="close()">Close</button>
        </mat-dialog-actions>
    `,
    styles: [`
        :host {
            display: block;
            padding: 1rem;
        }
        h2 {
            color: #d32f2f;
            margin: 0;
        }
        p {
            margin: 1rem 0;
            color: #333;
        }
    `]
})
export class ErrorDialogComponent {
    constructor(
        @Inject(MAT_DIALOG_DATA) public data: { message: string },
        private dialogRef: MatDialogRef<ErrorDialogComponent>
    ) {}

    close(): void {
        this.dialogRef.close();
    }
}