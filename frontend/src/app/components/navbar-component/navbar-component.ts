import {
  Component,
  ElementRef,
  ViewChild,
  inject,
  OnInit,
  AfterViewInit,
  OnDestroy,
} from '@angular/core';
import { Router, NavigationEnd, RouterLink } from '@angular/router';
import { Bell, LucideAngularModule } from 'lucide-angular';
import { filter } from 'rxjs/operators';
import { NotificationsService } from '../../service/notifications-service';
import { UserService } from '../../service/user-service';

interface notificationDTO {
  id: number;
  description: string;
  createdTime: string;
  isRead: boolean;
}

@Component({
  selector: 'app-navbar-component',
  standalone: true,
  imports: [RouterLink, LucideAngularModule],
  templateUrl: './navbar-component.html',
  styleUrls: ['./navbar-component.css'],
})
export class NavbarComponent implements OnInit, AfterViewInit, OnDestroy {
  isCreatePostPage = false;
  readonly BellIcon = Bell;
  totalNotifications = 0;
  showNotificationsModal = false;
  notifications: notificationDTO[] = [];
  notificationsPage = 0;

  @ViewChild('notificationsEnd') notificationsEnd?: ElementRef<HTMLDivElement>;
  private observer?: IntersectionObserver;

  private router = inject(Router);
  private notificationsService = inject(NotificationsService);
  private userService: UserService = inject(UserService);
  user: userResponse | null = null;
  
  ngOnInit(): void {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.isCreatePostPage = event.urlAfterRedirects.includes('/create-post');
      });

    this.notificationsService.getNotificationCount().subscribe({
      next: (res) => (this.totalNotifications = res),
      error: (err) => console.error(err),
    });
    this.userService.user$.subscribe((user) => (this.user = user));
  }

  ngAfterViewInit(): void {
    this.observer = new IntersectionObserver((entries) => {
      const entry = entries[0];
      if (entry.isIntersecting) {
        this.observer?.unobserve(entry.target);
        this.getNotifications();
      }
    });
  }

  toggleNotifications(): void {
    this.showNotificationsModal = !this.showNotificationsModal;

    if (this.showNotificationsModal) {
      this.notifications = [];
      this.notificationsPage = 0;
      this.getNotifications();

      // Allow DOM to render, then start observing
      setTimeout(() => {
        if (this.notificationsEnd?.nativeElement) {
          this.observer?.observe(this.notificationsEnd.nativeElement);
        }
      });
    } else {
      this.observer?.disconnect();
    }
  }

  getNotifications(): void {
    this.notificationsService.getNotifications(this.notificationsPage).subscribe({
      next: (res) => {
        if (res.length === 0) return; // no more results
        this.notifications = [...this.notifications, ...res];
        this.notificationsPage++;

        setTimeout(() => {
          if (this.notificationsEnd?.nativeElement) {
            this.observer?.observe(this.notificationsEnd.nativeElement);
          }
        });
      },
      error: (err) => console.error(err),
    });
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
  }
}
