import { Component, inject, OnInit } from '@angular/core';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { PostSectionComponent } from '../../components/post-section-component/post-section-component';
import { UserService } from '../../service/user-service';
import { UserSection } from '../../components/user-section/user-section';
import { LogIn, LogOut, LucideAngularModule, SendHorizontal, Settings, User } from 'lucide-angular';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth-service';

@Component({
  selector: 'app-home',
  imports: [NavbarComponent, PostSectionComponent, UserSection, LucideAngularModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  user: userResponse | null = null;
  private userService: UserService = inject(UserService);
  private router: Router = inject(Router);
  private authService = inject(AuthService);

  readonly profileIcon = User;
  readonly settingsIcon = Settings;
  readonly logoutIcon = LogOut;
  readonly sendIcon = SendHorizontal;

  goToProfile() {
    if (!this.user) return;
    this.router.navigate(['/profile', this.user?.id]);
  }

  goToSettings() {
    this.router.navigate(['/settings']);
  }

  logout() {
    this.authService.logout().subscribe(() => this.router.navigate(['/login']));
  }

  ngOnInit() {
    this.userService.user$.subscribe((user) => (this.user = user));
  }
}
