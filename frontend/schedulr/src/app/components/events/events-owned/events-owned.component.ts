import { Component, OnInit, inject, signal } from '@angular/core';
import {EventCompactComponent} from "../event-compact/event-compact.component";
import {EventService} from "../../../shared/services/event.service";
import {AuthService} from "../../../shared/services/auth.service";
import {EventReadOnly} from "../../../shared/interfaces/Event";

@Component({
  selector: 'app-events-owned',
  standalone: true,
  templateUrl: './events-owned.component.html',
  styleUrls: ['./events-owned.component.css'],
  imports: [EventCompactComponent]
})
export class EventsOwnedComponent implements OnInit {
  private eventService = inject(EventService);
  private authService = inject(AuthService);
  private userUuid = this.authService.loggedInUserUuid()
  
  
  events = signal<EventReadOnly[]>([]);
  filteredEvents = signal<EventReadOnly[]>([]);
  currentStatus = signal<'PENDING' | 'COMPLETED' | 'CANCELED'>('PENDING');
  
  ngOnInit() {
    this.loadAllOwnedEvents();
  }
  
  loadAllOwnedEvents() {
    this.eventService.getOwnedEvents(this.userUuid!).subscribe({
      next: (data) => {
        this.events.set(data);
        this.filterEvents('PENDING'); // Default to show pending events
      },
      error: (err) => console.error("Failed to fetch owned events", err)
    });
  }
  
  filterEvents(status: 'PENDING' | 'COMPLETED' | 'CANCELED') {
    this.currentStatus.set(status);
    this.filteredEvents.set(this.events().filter(event => event.status === status));
  }
}
