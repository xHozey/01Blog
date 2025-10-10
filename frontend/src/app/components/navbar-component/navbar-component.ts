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
import { CommonModule } from '@angular/common';

interface notificationDTO {
  id: number;
  description: string;
  createdTime: string;
  isRead: boolean;
}

@Component({
  selector: 'app-navbar-component',
  standalone: true,
  imports: [RouterLink, LucideAngularModule, CommonModule],
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
  isLoading = false;

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
    this.userService.fetchCurrentUser();
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
      this.isLoading = false;
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
    if (this.isLoading) return;
    this.isLoading = true;

    this.notificationsService.getNotifications(this.notificationsPage).subscribe({
      next: (res) => {
        if (res.length === 0) return;
        this.notifications = [...this.notifications, ...res];
        this.notificationsPage++;
        this.isLoading = false;
        setTimeout(() => {
          if (this.notificationsEnd?.nativeElement) {
            this.observer?.observe(this.notificationsEnd.nativeElement);
          }
        });
        console.log(res, res[0]?.isRead)
        const unreadIds = res.filter((n) => !n.isRead).map((n) => n.id);
        if (unreadIds.length > 0) this.notificationsService.readNotification(unreadIds).subscribe({
          next: (res) => {
            console.log(res)
          },
          error: (err) => {
            console.error(err)
          }
        });
      },
      error: (err) => {
        console.error(err);
        this.isLoading = false;
      },
    });
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
  }
}
