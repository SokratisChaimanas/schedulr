import { Component, DestroyRef, inject } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from "../../../shared/services/auth.service";
import { EventService } from "../../../shared/services/event.service";
import {EventCreate} from "../../../shared/interfaces/Event";
import {ErrorComponent} from "../../messages/error/error.component";

@Component({
  selector: 'app-create-event',
  standalone: true,
  imports: [ReactiveFormsModule, ErrorComponent],
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.css'],
})
export class CreateEventComponent {
  router = inject(Router);
  destroyRef = inject(DestroyRef);
  authService = inject(AuthService);
  eventService = inject(EventService);
  
  loggedInUserUuid = this.authService.loggedInUserUuid()
  
  categories = ['CONCERT', 'CONFERENCE', 'EXHIBITION', 'NETWORKING', 'PARTY', 'SPORTS'];
  
  form = new FormGroup({
    title: new FormControl('', [Validators.required, Validators.maxLength(100)]),
    description: new FormControl('', [Validators.required, Validators.maxLength(500)]),
    date: new FormControl('', {
      validators: [
        Validators.required,
        this.dateValidator
      ],
      updateOn: 'change'
    }),
    location: new FormControl('', [Validators.required]),
    price: new FormControl(null, [Validators.required, Validators.min(0)]),
    maxSeats: new FormControl(null, [
      Validators.required,
      Validators.min(1),
      this.wholeNumberValidator
    ]),
    category: new FormControl('CONCERT', [Validators.required]),
    imageFile: new FormControl<File | null>(null),
  });
  
  errorMessages = {
    title: {
      required: 'Event title is required',
      maxLength: 'Event title must be less than 100 characters'
    },
    description: {
      required: 'Description is required',
      maxLength: 'Description must be less than 500 characters'
    },
    date: {
      required: 'A date and time must be selected',
      dateError: 'The event date and time must be in the future'
    },
    location: {
      required: 'Location is required'
    },
    price: {
      required: 'Price is required',
      min: 'Price must be a positive number'
    },
    maxSeats: {
      required: 'Max seats is required',
      min: 'Max seats must be at least 1',
      wholeNumber: 'Max seats must be a whole number'
    },
    category: {
      required: 'Category is required'
    },
    imageFile: {
      invalidImage: 'Please provide a valid image file'
    }
  };
  
  onSubmit() {
    this.trimValues();
    this.form.markAllAsTouched();
    if (this.form.invalid) {
      return;
    }
    
    const createEvent: EventCreate = {
      title: this.form.value.title!,
      description: this.form.value.description!,
      date: this.form.value.date!,
      location: this.form.value.location!,
      price: this.form.value.price!,
      maxSeats: this.form.value.maxSeats!,
      category: this.form.value.category!,
    };
    
    const uuid = this.loggedInUserUuid!.toString();
    const imageFile = this.form.value.imageFile || undefined;
    
    const subscription = this.eventService.createEvent(createEvent, uuid, imageFile).subscribe({
      next: (response) => {
        this.router.navigate(['events'], {queryParams: {'event-created': true}});
      },
      error: (error) => {
        if (error.code === 'ImageInvalidArgument') {
          
            this.form.get('imageFile')?.setErrors({invalidImage: true});
            this.form.updateValueAndValidity();
            return;
        }
        },
    });
    
    this.destroyRef.onDestroy(() => subscription.unsubscribe());
  }
  
  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.form.patchValue({ imageFile: input.files[0] });
    }
  }
  
  trimValues() {
    ['title', 'description', 'date', 'location'].forEach(field => {
      const value = this.form.get(field)?.value;
      if (value) {
        this.form.get(field)?.setValue(value.trim());
      }
    });
  }
  
  private dateValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const value = control.value;
    
    if (!value) {
      return null;
    }
    
    const inputDate = new Date(value);
    const now = new Date();
    
    if (inputDate <= now) {
      return { dateError: true };
    }
    
    return null;
  }
  
  private wholeNumberValidator(control: AbstractControl) {
    return Number.isInteger(control.value) ? null : { wholeNumber: true };
  }
  
}
