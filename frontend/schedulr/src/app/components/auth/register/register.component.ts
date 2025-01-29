import {Component, DestroyRef, inject} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../../../shared/services/auth.service';
import {ErrorComponent} from '../../messages/error/error.component';
import {UserRegister} from "../../../shared/interfaces/User";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule,
    ErrorComponent,
    ReactiveFormsModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  authService = inject(AuthService);
  router = inject(Router);
  destroyRef = inject(DestroyRef);
  
  roles = ['SIMPLE_USER', 'ADMIN']; // Static roles list
  
  form = new FormGroup({
    username: new FormControl('', {
      validators: [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(20)
      ],
      updateOn: 'blur'
    }),
    email: new FormControl('', {
      validators: [
        Validators.required,
        Validators.email
      ],
      updateOn: 'blur'
    }),
    password: new FormControl('', {
      validators: [
        Validators.required,
        Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$')
      ],
      updateOn: 'blur'
    }),
    confirmPassword: new FormControl(''),
    firstname: new FormControl('', {
      validators: [Validators.required, Validators.maxLength(50)],
      updateOn: 'blur'
    }),
    lastname: new FormControl('', {
      validators: [Validators.required, Validators.maxLength(50)],
      updateOn: 'blur'
    }),
    role: new FormControl('SIMPLE_USER', {nonNullable: true})
  }, {
    validators: this.passwordsMatchValidator,
    updateOn: 'change'
  });
  
  errorMessages = {
    username: {
      required: 'Username is required',
      invalid: 'Username must be between 3-20 characters long',
      inUse: 'Username is already in use'
    },
    email: {
      required: 'Email is required',
      invalid: 'Please enter a valid email address',
      inUse: 'Email is already in use'
    },
    password: {
      required: 'Password is required',
      invalid: 'Password must contain at least 1 letter, 1 number, 1 symbol and have 8 characters'
    },
    general: {
      passwordMismatch: 'Confirm password does not match'
    }
  };
  
  onSubmit() {
    this.trimValues();
    this.form.markAllAsTouched();
    this.form.updateValueAndValidity();
    
    if (this.form.invalid) {
      return;
    }
    
    const userData: UserRegister = {
      username: this.form.value.username!,
      email: this.form.value.email!,
      password: this.form.value.password!,
      confirmPassword: this.form.value.confirmPassword!,
      firstname: this.form.value.firstname!,
      lastname: this.form.value.lastname!,
      role: this.form.value.role!
    };
    
    const subscription = this.authService.registerUser(userData).subscribe({
      next: response => {
        this.router.navigate(['login'], {queryParams: {'was-registered': response.data.uuid}});
      },
      error: error => {
        if (error.code === 'UserAlreadyExists') {
          
          if (error.message.includes('username ')) {
            this.form.get('username')?.setErrors({usernameExists: true});
            this.form.updateValueAndValidity();
            return;
          } else {
            this.form.get('email')?.setErrors({emailExists: true});
            this.form.updateValueAndValidity();
            return;
          }
        }
      }
    });
    
    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
  
  passwordsMatchValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const form = control as FormGroup;
    
    if (form.get('password')?.value !== form.get('confirmPassword')?.value) {
      return {passwordMismatch: true};
    }
    
    return null;
  }
  
  trimValues() {
    ['username', 'email', 'firstname', 'lastname'].forEach(field => {
      const value = this.form.get(field)?.value;
      if (value) {
        this.form.get(field)?.setValue(value.trim());
      }
    });
  }
}
