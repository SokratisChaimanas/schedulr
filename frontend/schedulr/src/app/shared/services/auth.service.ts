import { HttpClient } from '@angular/common/http';
import {Injectable, Inject, signal, inject} from '@angular/core';
import { map } from 'rxjs';
import {UserReadOnly, UserRegister} from "../interfaces/User";
import {AuthenticationRequest, AuthenticationResponse} from "../interfaces/Auth";


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private API_URL_AUTH = 'http://localhost:8080/auth'; // Update with your actual backend URL
  loggedInUser = signal<UserReadOnly | null>(null);
  
  constructor() {
    const authToken = localStorage.getItem('authToken');
    if (authToken) {
      this.loggedInUser.set(this.decodeToken(authToken));
    }
  }
  
  decodeToken(token: string): UserReadOnly {
    // Implement JWT decoding logic here (consider using a library like jwt-decode)
    // This example assumes a simplified structure for logged-in user data
    return { uuid: token.split('.')[0], username: token.split('.')[1] }; // Placeholder for actual decoding
  }
  
  registerUser(userdata: UserRegister) {
    return this.http.post<UserRegister>(`${this.API_URL_AUTH}/register`, userdata).pipe(
        map(response => response) // Assuming backend returns the created user data
    );
  }
  
  loginUser(credentials: AuthenticationRequest) {
    return this.http.post<AuthenticationResponse>(`${this.API_URL_AUTH}/login`, credentials).pipe(
        map(response => {
          this.storeToken(response.token);
          this.loggedInUser.set(this.decodeToken(response.token));
          return response;
        })
    );
  }
  
  storeToken(token: string) {
    localStorage.setItem('authToken', token);
  }
  
  removeToken() {
    localStorage.removeItem('authToken');
    this.loggedInUser.set(null);
  }
}