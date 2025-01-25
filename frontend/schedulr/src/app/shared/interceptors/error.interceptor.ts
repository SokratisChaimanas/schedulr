import { HttpErrorResponse, HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {ErrorService} from "../services/error.service";

export const httpErrorInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
    console.log('Went to error interceptor')
    const errorService = inject(ErrorService); // Inject ErrorService functionally within the interceptor
    
    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            if (error.status === 409 || error.status === 400) {
                return throwError(() => error.error);
            }
            
            // Handle other errors globally
            errorService.handleResponseError(error);
            return throwError(() => error.error); // Re-throw the error for logging or further handling if necessary
        })
    );
};