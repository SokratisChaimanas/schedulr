import { HttpClient } from '@angular/common/http';
import {Injectable, Inject, signal, inject} from '@angular/core';
import { map } from 'rxjs';
import {LoggedInUserData, UserReadOnly, UserRegister} from "../interfaces/User";
import {AuthenticationRequest, AuthenticationResponse} from "../interfaces/Auth";
import {jwtDecode} from "jwt-decode";
import {SuccessResponse} from "../interfaces/Response";


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private API_URL_AUTH = 'http://localhost:8080/api/auth';
  loggedInUser = signal<LoggedInUserData | null>(null);
  loggedInUserData = signal<AuthenticationResponse | null>(null);
  
  constructor() {
    const authToken = localStorage.getItem('authToken');
    if (authToken) {
      this.loggedInUser.set(this.decodeToken(authToken));
    }
  }
  
  decodeToken(token: string) {
    return jwtDecode(token).sub as unknown as LoggedInUserData;
  }
  
  registerUser(userdata: UserRegister) {
    return this.http.post<SuccessResponse<UserReadOnly>>(`${this.API_URL_AUTH}/register`, userdata).pipe(
        map(response => response)
    );
  }
  
  loginUser(credentials: AuthenticationRequest) {
    return this.http.post<SuccessResponse<AuthenticationResponse>>(`${this.API_URL_AUTH}/login`, credentials).pipe(
        map(response => {
          this.storeToken(response.data.token);
          this.loggedInUser.set(this.decodeToken(response.data.token));
          this.loggedInUserData.set(response.data);
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