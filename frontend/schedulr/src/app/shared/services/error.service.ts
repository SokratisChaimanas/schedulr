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
            const failureResponse = error.error; // Assuming the error body matches FailureResponseDTO
            switch (failureResponse.code) {
                case 'ValidationError':
                    this.handleValidationErrors(failureResponse.extraInformation);
                    break;
                case 'AppObjectNotFound':
                    alert(`Not found: ${failureResponse.message}`);
                    break;
                case 'AppObjectAlreadyExists':
                    alert(`Conflict: ${failureResponse.message}`);
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
                    alert(`Error: ${failureResponse.message}`);
                    break;
            }
        } else {
            this.handleGenericError(error);
        }
    }
    
    private handleValidationErrors(errors: Record<string, string>) {
        const errorMessages = Object.entries(errors)
            .map(([field, message]) => `${field}: ${message}`)
            .join('\n');
        alert(`Validation errors:\n${errorMessages}`);
    }
    
    private handleGenericError(error: HttpErrorResponse) {
        switch (error.status) {
            case 401:
                alert('Unauthorized. Please log in.');
                this.router.navigate(['/login']);
                break;
            case 500:
                alert('Internal server error. Please try again later.');
                break;
            case 0:
                alert('Backend server is not reachable.');
                break;
            default:
                alert('Unexpected error occurred. Please try again.');
                break;
        }
    }
}
