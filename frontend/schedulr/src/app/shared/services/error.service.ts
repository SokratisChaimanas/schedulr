import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class ErrorService {
    private router = inject(Router);
    
    handleResponseError(error: HttpErrorResponse) {
        if (error.error && error.error.code) {
            const failureResponse = error.error;
            switch (failureResponse.code) {
                case 'AppObjectNotFound':
                    this.router.navigate(['/resource-not-found']);
                    break;
                case 'AccessDenied':
                    alert('Access denied: ' + failureResponse.message);
                    break;
                case 'EventFull':
                    alert(`Event full: ${failureResponse.message}`);
                    break;
                case 'AppObjectUnavailable':
                    alert(`Unavailable: ${failureResponse.message}`);
                    break;
                default:
                    this.router.navigate(['/unexpected-error']);
                    break;
            }
        }
    }
    
    private handleValidationErrors(errors: Record<string, string>) {
        const errorMessages = Object.entries(errors)
            .map(([field, message]) => `${field}: ${message}`)
            .join('\n');
        alert(`Validation errors:\n${errorMessages}`);
    }
}
