import { Routes } from '@angular/router';
import {EventsComponent} from "./components/events/events.component";


export const routes: Routes = [
    {path: '', redirectTo: '/events', pathMatch: 'full'},
    { path: 'events', component: EventsComponent },
    // { path: 'events/create', component: EventCreateComponent },
    // { path: 'login', component: LoginComponent },
    // { path: 'register', component: RegisterComponent },
    // { path: '**', component: NotFoundComponent },
];
