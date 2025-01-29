import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from "@angular/forms";
import { AuthService } from "../../../shared/services/auth.service";
import { AuthenticationRequest } from "../../../shared/interfaces/Auth";
import {SuccessComponent} from "../../messages/success/success.component";
import {ErrorComponent} from "../../messages/error/error.component";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule,
    SuccessComponent,
    ErrorComponent,
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);
  
  wasRegistered = signal<boolean>(false);
  areCredentialsInvalid = signal<boolean>(false);
  
  form = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  });
  
  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      if (params.get('invalid-credentials')) {
        this.areCredentialsInvalid.set(true)
        this.wasRegistered.set(false)
        return;
      }
      if (params.get('was-registered')) this.wasRegistered.set(true)
    });
  }
  
  onSubmit() {
    const credentials: AuthenticationRequest = {
      username: this.form.value.username!,
      password: this.form.value.password!
    };
    
    this.authService.loginUser(credentials).subscribe({
      next: (response) => {
        this.router.navigate(['/events'])
      },
      error: (error) => {
        if (error.status === 401 || error.code === 'InvalidCredentials') {
          this.router.navigate(['login'], { queryParams: { 'invalid-credentials': true } });
        }
      }
    });
  }
}
