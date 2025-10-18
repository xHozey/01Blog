import {
  Component,
  ElementRef,
  ViewChild,
  inject,
  OnInit,
  AfterViewInit,
  OnDestroy,
  HostListener,
} from '@angular/core';
import { Router, NavigationEnd, RouterLink } from '@angular/router';
import { Bell, LucideAngularModule } from 'lucide-angular';
import { filter } from 'rxjs/operators';
import { NotificationsService } from '../../service/notifications-service';
import { UserService } from '../../service/user-service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../service/auth-service';
import { parseApiError } from '../../utils/errorHelper';
import { ToastService } from '../../service/toast-service';
import { ThemeToggleComponent } from '../theme-toggle-component/theme-toggle-component';

@Component({
  selector: 'app-navbar-component',
  standalone: true,
  imports: [RouterLink, LucideAngularModule, CommonModule, ThemeToggleComponent],
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
  activeTab: 'all' | 'unread' = 'all';

  @ViewChild('notificationsEnd') notificationsEnd?: ElementRef<HTMLDivElement>;
  @ViewChild('notificationContainer') notificationContainer?: ElementRef<HTMLLIElement>;

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    if (!this.showNotificationsModal) return;

    const target = event.target as HTMLElement;
    const notificationElement = this.notificationContainer?.nativeElement;

    if (notificationElement && !notificationElement.contains(target)) {
      this.showNotificationsModal = false;
    }
  }

  private observer?: IntersectionObserver;

  private router = inject(Router);
  private notificationsService = inject(NotificationsService);
  private userService: UserService = inject(UserService);
  private authService: AuthService = inject(AuthService);
  private toastService = inject(ToastService);
  user: userResponse | null = null;

  ngOnInit(): void {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.isCreatePostPage = event.urlAfterRedirects.includes('/create-post');
      });

    this.countNotifications();
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

  get filteredNotifications(): notificationDTO[] {
    return this.activeTab === 'all'
      ? this.notifications
      : this.notifications.filter((n) => !n.isRead);
  }

  toggleNotifications(): void {
    this.showNotificationsModal = !this.showNotificationsModal;

    if (this.showNotificationsModal) {
      this.notifications = [];
      this.notificationsPage = 0;
      this.isLoading = false;
      this.getNotifications();
      setTimeout(() => {
        if (this.notificationsEnd?.nativeElement) {
          this.observer?.observe(this.notificationsEnd.nativeElement);
        }
      });
    } else {
      this.countNotifications();
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
        const unreadIds = res.filter((n) => !n.isRead).map((n) => n.id);
        if (unreadIds.length > 0)
          this.notificationsService.readNotification(unreadIds).subscribe({
            next: (res) => {
              console.log(res);
            },
            error: (err) => {
              parseApiError(err).forEach((msg) => this.toastService.error(msg));
            },
          });
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
        this.isLoading = false;
      },
    });
  }

  onLogout() {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  countNotifications() {
    this.notificationsService.getNotificationCount().subscribe({
      next: (res) => (this.totalNotifications = res),
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
  }

  goToPost(id: number) {
    this.router.navigate(['/post', id]);
  }

  goToUser(id: number) {
    this.router.navigate(['/profile', id]);
  }
}
