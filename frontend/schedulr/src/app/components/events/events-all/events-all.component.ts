import { Component, inject, signal } from '@angular/core';
import { EventCompactComponent } from "../event-compact/event-compact.component";
import { EventService } from "../../../shared/services/event.service";
import { AuthService } from "../../../shared/services/auth.service";
import { EventReadOnly } from "../../../shared/interfaces/Event";

@Component({
  selector: 'app-events-all',
  standalone: true,
  templateUrl: './events-all.component.html',
  styleUrls: ['./events-all.component.css'],
  imports: [EventCompactComponent]
})
export class EventsAllComponent {
  private eventService = inject(EventService);
  private authService = inject(AuthService);
  
  events = signal<EventReadOnly[]>([]);
  currentStatus = signal<'PENDING' | 'COMPLETED' | 'CANCELED'>('PENDING');
  currentPage = signal<number>(0);
  pageSize = 10;
  
  ngOnInit() {
    this.loadEvents('PENDING'); // Default to pending events
  }
  
  loadEvents(status: 'PENDING' | 'COMPLETED' | 'CANCELED', page: number = 0) {
    this.currentStatus.set(status);
    this.currentPage.set(page);
    
    this.eventService.getEventsByStatus(this.authService.loggedInUserUuid()!, status).subscribe({
      next: (data) => {
        this.events.set(data); // Set the retrieved events
      },
      error: (err) => console.error(`Failed to fetch ${status} events`, err)
    });
  }
  
  loadNextPage() {
    const nextPage = this.currentPage() + 1;
    this.loadEvents(this.currentStatus(), nextPage);
  }
  
  loadPreviousPage() {
    const previousPage = Math.max(this.currentPage() - 1, 0);
    this.loadEvents(this.currentStatus(), previousPage);
  }
}
