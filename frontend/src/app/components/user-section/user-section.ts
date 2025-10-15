import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../service/user-service';
import { ToastService } from '../../service/toast-service';
import { parseApiError } from '../../utils/errorHelper';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { FollowService } from '../../service/follow-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-section',
  standalone: true,
  imports: [CommonModule, InfiniteScrollDirective],
  templateUrl: './user-section.html',
  styleUrls: ['./user-section.css'],
})
export class UserSection implements OnInit {
  users: userSuggetion[] = [];

  private userService = inject(UserService);
  private toastService = inject(ToastService);
  private followService = inject(FollowService);
  private router = inject(Router);

  page: number = 0;
  isLoading = false;
  allLoaded = false;

  ngOnInit(): void {
    this.loadMore();
  }

  loadMore() {
    if (this.isLoading || this.allLoaded) return;

    this.isLoading = true;
    this.userService.getUserSuggestions(this.page).subscribe({
      next: (res) => {
        if (res.length === 0) {
          this.allLoaded = true;
        } else {
          this.users = [...this.users, ...res];
          this.page++;
        }
        this.isLoading = false;
      },
      error: (err) => {
        this.allLoaded = true;
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
        this.isLoading = false;
      },
    });
  }

  onFollow(id: number) {
    this.followService.followUser(id).subscribe({
      next: () => {
        const user = this.users.find((u) => u.userResponse.id === id);
        if (user) {
          user.isFollowing = !user.isFollowing;
        }
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  goToProfile(id: number) {
    this.router.navigate(['/profile', id]);
  }
}
