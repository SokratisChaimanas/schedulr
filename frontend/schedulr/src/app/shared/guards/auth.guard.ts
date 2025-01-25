import {CanActivateFn, Router} from "@angular/router";
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";

export const authGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);
    
    if (authService.loggedInUser()) {
        return true;
    }
    
    router.navigate(['login']);
    return false;
};

export const loggedInGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);
    
    if (!authService.loggedInUser()) {
        return true;
    }
    
    router.navigate(['tasks']);
    return false;
}