import {Component, inject, OnInit, signal} from '@angular/core';
import {EventService} from "../../shared/services/event.service";
import {EventReadOnly} from "../../shared/interfaces/Event";
import {EventCompactComponent} from "./event-compact/event-compact.component";
import {AuthService} from "../../shared/services/auth.service";

@Component({
  selector: 'app-events',
  standalone: true,
  templateUrl: './events.component.html',
  imports: [
    EventCompactComponent
  ],
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {
  eventService = inject(EventService);
  authService = inject(AuthService)
  
  events = signal<EventReadOnly[]>([]);
  totalPages = signal<number>(0);
  currentPage = signal<number>(0);
  pageSize = signal<number>(10);
  
  
  ngOnInit() {
    this.loadEvents(this.currentPage());
  }
  
  loadEvents(page: number) {
    this.eventService.getPaginatedEvents(page, this.pageSize() , this.authService.loggedInUserUuid()!).subscribe({
      next: response => {
        this.events.set(response.content);
        console.log(response.content)
        this.totalPages.set(response.totalPages);
        this.currentPage.set(response.number);
      },
      error: error => {
        console.error("Error fetching events:", error);
      }
    });
  }
  
  nextPage() {
    if (this.currentPage() < this.totalPages() - 1) {
      this.loadEvents(this.currentPage() + 1);
    }
  }
  
  prevPage() {
    if (this.currentPage() > 0) {
      this.loadEvents(this.currentPage() - 1);
    }
  }
}
