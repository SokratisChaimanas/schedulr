import { HttpErrorResponse, HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

export const httpErrorInterceptor: HttpInterceptorFn = (
    req: HttpRequest<unknown>,
    next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
    const router = inject(Router);
    
    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            console.log('Error intercepted:', error);
            
            // Use error.status directly (not error.error.status)
            if (error.status === 403) {
                router.navigate(['resource-forbidden']);
                return throwError(() => error.error);
            }
            
            if (error.status === 404) {
                router.navigate(['resource-not-found']);
                return throwError(() => error.error);
            }
            
            if (error.status === 409 || error.status === 400) {
                console.log('STATUS:', error.error);
                
                if (error.error.code) {
                    // Ensure detail exists and is a string before checking `includes`
                    if (error.error.code === 'InvalidCredentials' ||
                        error.error.code === 'ImageInvalidArgument' ||
                        error.error.code.includes('AlreadyExists')) {
                        return throwError(() => error.error);
                    }
                }
                
                router.navigate(['bad-request']);
                return throwError(() => error.error);
            }
            router.navigate(['unexpected-error']);
            
            return throwError(() => error.error);
        })
    );
};
