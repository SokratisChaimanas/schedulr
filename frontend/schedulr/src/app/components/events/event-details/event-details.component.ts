import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { EventService } from "../../../shared/services/event.service";
import { EventReadOnly } from "../../../shared/interfaces/Event";
import { EventCompactComponent } from "../event-compact/event-compact.component";
import { CommentComponent } from "./comment/comment.component";
import { AuthService } from "../../../shared/services/auth.service";

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  standalone: true,
  imports: [DatePipe, CurrencyPipe, EventCompactComponent, CommentComponent],
  styleUrls: ['./event-details.component.css']
})
export class EventDetailsComponent implements OnInit {
  
  eventService = inject(EventService);
  authService = inject(AuthService);
  router = inject(Router);
  route = inject(ActivatedRoute);
  
  event: EventReadOnly | undefined;
  loading: boolean = true;
  error: string = '';
  eventUuid: string | null = '';
  userUuid = this.authService.loggedInUserUuid();
  isAttending: boolean = false;
  
  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      if (params.get('uuid')) {
        this.eventUuid = params.get('uuid');
      }
    });
    
    if (this.eventUuid) {
      this.loadEventDetails();
      this.checkIfUserIsAttending();
    } else {
      this.router.navigate(['resource-not-found']);
    }
  }
  
  private loadEventDetails() {
    this.eventService.getEventByUuid(this.eventUuid!).subscribe({
      next: (event) => {
        this.event = event;
        this.loading = false;
      },
      error: (err) => {
        console.log(err);
      }
    });
  }
  
  private checkIfUserIsAttending() {
    this.eventService.getPendingAttendedEvents(this.userUuid!).subscribe({
      next: (attendedEvents) => {
        this.isAttending = attendedEvents.some(event => event.uuid === this.eventUuid);
      },
      error: (err) => {
        console.log('Error fetching attended events:', err);
      }
    });
  }
  
  attendEvent() {
    if (this.eventUuid) {
      const attendDTO = {
        userUuid: this.userUuid!,
        eventUuid: this.eventUuid
      };
      
      this.eventService.attendEvent(attendDTO).subscribe({
        next: (response) => {
          console.log('Successfully attended the event:', response);
          this.isAttending = true;
        },
        error: (err) => {
          console.log('Error attending event:', err);
        }
      });
    }
  }
}
