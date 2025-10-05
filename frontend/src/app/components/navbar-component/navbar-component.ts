import { Component } from '@angular/core';
import { Router, NavigationEnd, RouterLink } from '@angular/router';
import { Bell, LucideAngularModule } from 'lucide-angular';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-navbar-component',
  standalone: true,
  imports: [RouterLink, LucideAngularModule],
  templateUrl: './navbar-component.html',
  styleUrls: ['./navbar-component.css'], 
})
export class NavbarComponent {
  isCreatePostPage = false; 
  readonly BellIcon = Bell
  constructor(private router: Router) {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.isCreatePostPage = event.urlAfterRedirects.includes('/create-post');
      });
  }
}