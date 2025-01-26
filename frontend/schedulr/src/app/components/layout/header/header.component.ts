import {Component, inject} from '@angular/core';
import { RouterModule } from '@angular/router';
import {AuthService} from "../../../shared/services/auth.service";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent {
  authService = inject(AuthService);
  
  loggedInUser = this.authService.loggedInUserUsername;
  
  onLogout() {
    this.authService.removeToken();
    this.authService.loggedInUserUsername.set(null)
  }
}
