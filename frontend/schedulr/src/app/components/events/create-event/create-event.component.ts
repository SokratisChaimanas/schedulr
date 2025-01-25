import { Component, DestroyRef, inject } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService } from "../../../shared/services/auth.service";
import { EventService } from "../../../shared/services/event.service";

@Component({
  selector: 'app-create-event',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.css'],
})
export class CreateEventComponent {
  router = inject(Router);
  http = inject(HttpClient);
  destroyRef = inject(DestroyRef);
  authService = inject(AuthService);
  eventService = inject(EventService);
  
  loggedInUserUuid = this.authService.loggedInUserData()?.uuid;
  
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
    maxSeats: new FormControl(null, [Validators.required, Validators.min(1)]),
    category: new FormControl('', [Validators.required]),
    imageFile: new FormControl<File | null>(null),
  });
  
  onSubmit() {
    this.trimValues();
    this.form.markAllAsTouched();
    if (this.form.invalid) {
      return;
    }
    
    const createEvent = {
      title: this.form.value.title!,
      description: this.form.value.description!,
      date: this.form.value.date!,
      location: this.form.value.location!,
      price: this.form.value.price!,
      maxSeats: this.form.value.maxSeats!,
      category: this.form.value.category!,
    };
    
    const uuid = this.loggedInUserUuid!;
    const imageFile = this.form.value.imageFile || undefined;
    // console.log(imageFile)
    
    const formData = new FormData();
    formData.append('createDTO', JSON.stringify(createEvent));
    formData.append('uuid', uuid);
    if (imageFile) {
      formData.append('imageFile', imageFile);
    }
    
    console.log(formData)
    
    const subscription = this.eventService.createEvent(createEvent, uuid, imageFile).subscribe({
      next: (response) => {
        console.log('Event created successfully:', response);
        this.router.navigate(['/events']);
      },
      error: (error) => {
        console.error('Error creating event:', error);
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
  
  dateValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const value = control.value;
    
    if (!value) {
      return null;
    }
    
    const inputDate = new Date(value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    if (inputDate < today) {
      return { dateError: true };
    }
    return null;
  }
}
