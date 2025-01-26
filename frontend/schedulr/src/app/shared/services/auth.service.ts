import { HttpClient } from '@angular/common/http';
import {Injectable, Inject, signal, inject} from '@angular/core';
import { map } from 'rxjs';
import {LoggedInUserData, UserReadOnly, UserRegister} from "../interfaces/User";
import {AuthenticationRequest, AuthenticationResponse, JwtPayload} from "../interfaces/Auth";
import {jwtDecode} from "jwt-decode";
import {SuccessResponse} from "../interfaces/Response";


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private API_URL_AUTH = 'http://localhost:8080/api/auth';
  loggedInUserUsername = signal<string | null>(null);
  loggedInUserUuid = signal<string | null>(null);
  loggedInUserRole = signal<string | null>(null);
  constructor() {
    const authToken = localStorage.getItem('authToken');
    if (authToken) {
      this.decodeToken(authToken);
    }
  }
  
  decodeToken(token: string) {
    const decodedToken = jwtDecode<JwtPayload>(token);
   
    if (decodedToken) {
      this.loggedInUserUsername.set(decodedToken.sub!); // Map the subject to username
      this.loggedInUserUuid.set(decodedToken.uuid!); // Extract UUID
      this.loggedInUserRole.set(decodedToken.role!); // Extract Role
    } else {
      console.error('Failed to decode token');
    }
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
          this.decodeToken(response.data.token)
          console.log('USER UUID: ' + this.loggedInUserUuid)
          return response;
        })
    );
  }
  
  storeToken(token: string) {
    localStorage.setItem('authToken', token);
  }
  
  removeToken() {
    localStorage.removeItem('authToken');
    this.loggedInUserUsername.set(null);
  }
}