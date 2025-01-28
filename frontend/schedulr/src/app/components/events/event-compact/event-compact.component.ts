import {Component, inject, Input} from '@angular/core';
import {EventReadOnly} from "../../../shared/interfaces/Event";
import {CurrencyPipe, DatePipe} from "@angular/common";
import {Router} from "@angular/router";

@Component({
  selector: 'app-event-compact',
  standalone: true,
  imports: [
    DatePipe,
    CurrencyPipe
  ],
  templateUrl: './event-compact.component.html',
  styleUrl: './event-compact.component.css'
})
export class EventCompactComponent {
  @Input({ required: true }) event!: EventReadOnly;
  
  router = inject(Router);
  
  isEventFull(): boolean {
    return this.event.bookedSeats === this.event.maxSeats;
  }
  
  onClick() {
     this.router.navigate(['/event'], { queryParams: { 'uuid': this.event.uuid } });
  }
}
