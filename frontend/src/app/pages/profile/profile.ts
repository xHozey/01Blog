import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../service/user-service';
import { FollowService } from '../../service/follow-service';
import { ActivatedRoute } from '@angular/router';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { CommonModule } from '@angular/common';
import { UserPosts } from '../../components/user-posts/user-posts';
import { ReportModalComponent } from '../../components/report-modal-component/report-modal-component';
import { ToastService } from '../../service/toast-service';
import { parseApiError } from '../../utils/errorHelper';

@Component({
  selector: 'app-profile',
  imports: [NavbarComponent, CommonModule, UserPosts, ReportModalComponent],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  private userService = inject(UserService);
  private followService = inject(FollowService);
  private route = inject(ActivatedRoute);
  private toastService = inject(ToastService);
  userId!: number;

  currentUser: userResponse | null = null;
  user!: userResponse;
  isFollowing = false;
  followersCount = 0;
  followingCount = 0;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.userId = id ? +id : 0;
    this.userService.getUserById(this.userId).subscribe({
      next: (res) => {
        this.user = res;
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
    this.userService.user$.subscribe((user) => (this.currentUser = user));

    this.followService.getFollowersCount(this.userId).subscribe({
      next: (res) => {
        this.followersCount = res;
      },
      error: (err) => parseApiError(err).forEach((msg) => this.toastService.error(msg)),
    });
    this.followService.getFollowedCount(this.userId).subscribe({
      next: (res) => {
        this.followingCount = res;
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
    if (this.userId != this.currentUser?.id) {
      this.followService.checkAlreadyFollowed(this.userId).subscribe({
        next: (res) => {
          this.isFollowing = res;
        },
        error: (err) => {
          parseApiError(err).forEach((msg) => this.toastService.error(msg));
        },
      });
    }
  }

  toggleFollow() {
    this.followService.followUser(this.userId).subscribe({
      next: () => {
        this.isFollowing = !this.isFollowing;
        if (!this.isFollowing) this.followersCount--;
        else this.followersCount++;
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }
  
  showReportModal = false;
  targetId = 0;
  onReport() {
    this.showReportModal = true;
    this.targetId = this.userId;
  }
}
