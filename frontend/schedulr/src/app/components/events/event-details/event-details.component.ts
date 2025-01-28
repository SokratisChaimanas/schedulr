import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { EventService } from '../../../shared/services/event.service';
import {EventCancel, EventReadOnly} from '../../../shared/interfaces/Event';
import { CommentService } from '../../../shared/services/comment.service';
import { AuthService } from '../../../shared/services/auth.service';
import { CommentCreate, CommentReadOnly } from '../../../shared/interfaces/Comment';
import { CommentComponent } from './comment/comment.component';
import { FormsModule } from '@angular/forms';
import {UserReadOnly} from "../../../shared/interfaces/User";

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  standalone: true,
  imports: [DatePipe, CurrencyPipe, CommentComponent, FormsModule],
  styleUrls: ['./event-details.component.css'],
})
export class EventDetailsComponent implements OnInit {
  eventService = inject(EventService);
  commentService = inject(CommentService);
  authService = inject(AuthService);
  router = inject(Router);
  route = inject(ActivatedRoute);
  
  event: EventReadOnly | undefined;
  loading: boolean = true;
  error: string = '';
  eventUuid: string | null = '';
  eventOwner: UserReadOnly | undefined
  userUuid = this.authService.loggedInUserUuid();
  isAttending: boolean = false;
  
  newComment: string = ''; // Text for the new comment
  comments: CommentReadOnly[] = []; // To store comments and dynamically update the section
  
  ngOnInit() {
    this.route.queryParamMap.subscribe((params) => {
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
        this.comments = event.commentsList
            ?.sort(
                (a: CommentReadOnly, b: CommentReadOnly) =>
                    new Date(b.date).getTime() - new Date(a.date).getTime()
            ) || [];
        this.eventOwner = this.event?.ownerReadOnlyDTO
        // console.log(this.eventOwner?.username)
        this.loading = false;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
  
  private checkIfUserIsAttending() {
    this.eventService.getPendingAttendedEvents(this.userUuid!).subscribe({
      next: (attendedEvents) => {
        this.isAttending = attendedEvents.some(
            (event) => event.uuid === this.eventUuid
        );
      },
      error: (err) => {
        console.log('Error fetching attended events:', err);
      },
    });
  }
  
  public isUserOwner() {
    return this.event?.ownerReadOnlyDTO.uuid === this.userUuid
  }
  
  attendEvent() {
    if (this.eventUuid) {
      const attendDTO = {
        userUuid: this.userUuid!,
        eventUuid: this.eventUuid,
      };
      
      this.eventService.attendEvent(attendDTO).subscribe({
        next: (response) => {
          console.log('Successfully attended the event:', response);
          this.isAttending = true;
          
          // Update the event to reflect the new seat count
          if (this.event) {
            this.event.bookedSeats++;
          }
        },
        error: (err) => {
          console.log('Error attending event:', err);
        },
      });
    }
  }
  
  addComment() {
    if (this.newComment.trim() && this.eventUuid) {
      const commentDTO: CommentCreate = {
        description: this.newComment,
        userUuid: this.userUuid!,
        eventUuid: this.eventUuid,
      };
      
      this.commentService.createComment(commentDTO).subscribe({
        next: (newComment) => {
          this.comments.push(newComment); // Update comments section
          this.newComment = ''; // Clear the text box
        },
        error: (err) => {
          console.log('Error adding comment:', err);
        },
      });
    }
  }
  
  cancelEvent() {
    if (this.isUserOwner() || this.authService.loggedInUserRole() === 'ADMIN') {
      const cancelDTO : EventCancel = {
        eventUuid: this.eventUuid!,
        userUuid: this.userUuid!
      };
      
      this.eventService.cancelEvent(cancelDTO).subscribe({
        next: (response) => {
          console.log('Event successfully canceled:', response);
          if (this.event) {
            this.event.status = 'CANCELED'; // Update the status locally
          }
        },
        error: (err) => {
          console.log('Error canceling event:', err);
        },
      });
    }
  }
  
  onCommentDeleted(commentUuid: string) {
    this.event!.commentsList =  this.event!.commentsList.filter(comment => comment.uuid !== commentUuid);
  }
  
}
