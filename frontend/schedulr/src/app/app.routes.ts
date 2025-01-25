import { Routes } from '@angular/router';
import {EventsComponent} from "./components/events/events.component";
import {authGuard, loggedInGuard} from "./shared/guards/auth.guard";
import {LoginComponent} from "./components/auth/login/login.component";
import {ResourceNotFoundComponent} from "./components/error-pages/resource-not-found/resource-not-found.component";
import {RegisterComponent} from "./components/auth/register/register.component";
import {
    UnexpectedErrorComponent
} from "./components/error-pages/unexpected-error/unexpected-error.component";


export const routes: Routes = [
    {path: '', redirectTo: '/events', pathMatch: 'full'},
    { path: 'events', component: EventsComponent, canActivate: [authGuard] },
    // { path: 'events/create', component: EventCreateComponent },
    { path: 'login', component: LoginComponent, canActivate: [loggedInGuard] },
    { path: 'register', component: RegisterComponent },
    { path: 'resource-not-found', component: ResourceNotFoundComponent, canActivate: [authGuard]},
    { path: 'unexpected-error', component: UnexpectedErrorComponent},
    
    { path: '**', component: ResourceNotFoundComponent },
];
