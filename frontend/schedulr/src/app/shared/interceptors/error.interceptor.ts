import { HttpErrorResponse, HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {ErrorService} from "../services/error.service";

export const httpErrorInterceptor: HttpInterceptorFn = (
    req: HttpRequest<unknown>,
    next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
    const errorService = inject(ErrorService);
    
    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            console.log('Intercepted HTTP error', error);
            
            // Check if the error is a structured response (FailureResponseDTO)
            if (error.error && error.error.code) {
                errorService.handleResponseError(error);
            } else {
                // Handle non-structured or unexpected errors
                errorService.handleResponseError(error);
            }
            
            return throwError(() => error);
        })
    );
};
