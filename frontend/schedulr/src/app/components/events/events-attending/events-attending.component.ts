import { Component, inject, signal } from '@angular/core';
import {EventCompactComponent} from "../event-compact/event-compact.component";
import {EventService} from "../../../shared/services/event.service";
import {AuthService} from "../../../shared/services/auth.service";
import {EventReadOnly} from "../../../shared/interfaces/Event";


@Component({
  selector: 'app-events-attending',
  standalone: true,
  templateUrl: './events-attending.component.html',
  styleUrls: ['./events-attending.component.css'],
  imports: [EventCompactComponent]
})
export class EventsAttendingComponent {
  private eventService = inject(EventService);
  private authService = inject(AuthService);
  private userUuid = this.authService.loggedInUserUuid();
  
  events = signal<EventReadOnly[]>([]);
  currentStatus = signal<'PENDING' | 'COMPLETED' | 'CANCELED'>('PENDING');
  
  ngOnInit() {
    this.loadEvents('PENDING'); // Default to pending events
  }
  
  loadEvents(status: 'PENDING' | 'COMPLETED' | 'CANCELED') {
    if (!this.userUuid) return;
    
    this.currentStatus.set(status);
    
    let fetchMethod;
    if (status === 'PENDING') {
      fetchMethod = this.eventService.getPendingAttendedEvents(this.userUuid);
    } else if (status === 'COMPLETED') {
      fetchMethod = this.eventService.getCompletedAttendedEvents(this.userUuid);
    } else {
      fetchMethod = this.eventService.getCanceledAttendedEvents(this.userUuid);
    }
    
    fetchMethod.subscribe({
      next: (data) => {
        this.events.set(data);
      },
      error: (err) => console.error(`Failed to fetch ${status} events`, err)
    });
  }
}
