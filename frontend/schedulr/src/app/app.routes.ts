import { Routes } from '@angular/router';
import {EventsComponent} from "./components/events/events.component";
import {authGuard, isAdminGuard, loggedInGuard} from "./shared/guards/auth.guard";
import {LoginComponent} from "./components/auth/login/login.component";
import {ResourceNotFoundComponent} from "./components/error-pages/resource-not-found/resource-not-found.component";
import {RegisterComponent} from "./components/auth/register/register.component";
import {
    UnexpectedErrorComponent
} from "./components/error-pages/unexpected-error/unexpected-error.component";
import {CreateEventComponent} from "./components/events/create-event/create-event.component";
import {EventDetailsComponent} from "./components/events/event-details/event-details.component";
import {EventsOwnedComponent} from "./components/events/events-owned/events-owned.component";
import {EventsAttendingComponent} from "./components/events/events-attending/events-attending.component";
import {
    ResourceForbidden
} from "./components/error-pages/resource-not-availabe/resource-forbidden";
import {EventsAllComponent} from "./components/events/events-all/events-all.component";


export const routes: Routes = [
    { path: '', redirectTo: '/events', pathMatch: 'full' },
    { path: 'events', component: EventsComponent, canActivate: [authGuard] },
    { path: 'events/create', component: CreateEventComponent, canActivate: [authGuard] },
    { path: 'events/owned', component: EventsOwnedComponent, canActivate: [authGuard] },
    { path: 'events/attending', component: EventsAttendingComponent, canActivate: [authGuard] },
    { path: 'events/all', component: EventsAllComponent, canActivate: [authGuard, isAdminGuard] },
    { path: 'event', component: EventDetailsComponent },
    { path: 'login', component: LoginComponent, canActivate: [loggedInGuard] },
    { path: 'register', component: RegisterComponent, canActivate: [loggedInGuard] },
    { path: 'resource-not-found', component: ResourceNotFoundComponent, canActivate: [authGuard]},
    { path: 'resource-forbidden', component: ResourceForbidden, canActivate: [authGuard]},
    { path: 'unexpected-error', component: UnexpectedErrorComponent},
    
    { path: '**', component: ResourceNotFoundComponent },
];
