import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../service/user-service';
import { FollowService } from '../../service/follow-service';
import { ActivatedRoute } from '@angular/router';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { CommonModule } from '@angular/common';
import { UserPosts } from '../../components/user-posts/user-posts';
import { ReportModalComponent } from '../../components/report-modal-component/report-modal-component';

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
        console.error(err);
      },
    });
    this.followService.getFollowersCount(this.userId).subscribe({
      next: (res) => {
        this.followersCount = res;
      },
      error: (err) => console.error(err),
    });
    this.followService.getFollowedCount(this.userId).subscribe({
      next: (res) => {
        this.followingCount = res;
      },
      error: (err) => {
        console.error(err);
      },
    });
    this.followService.checkAlreadyFollowed(this.userId).subscribe({
      next: (res) => {
        this.isFollowing = res;
      },
      error: (err) => {
        console.error(err);
      },
    });
    this.userService.fetchCurrentUser();
    this.userService.user$.subscribe((user) => (this.currentUser = user));
  }

  toggleFollow() {
    this.followService.followUser(this.userId).subscribe({
      next: (res) => {
        this.isFollowing = !this.isFollowing;
        if (!this.isFollowing) this.followersCount--;
        else this.followersCount++;
      },
      error: (err) => {
        console.error(err);
      },
    });
  }
  showReportModal = false;
  reportType: 'post' | 'comment' | 'user' = 'user';
  targetId = 0;
  onReport() {
    this.showReportModal = true;
    this.targetId = this.userId;
  }

  handleReportSubmit($event: { type: string; targetId?: number; description: string }) {
    if ($event.targetId == null) return;
    const payload: reportRequest = {
      id: $event.targetId,
      description: $event.description,
    };

    this.userService.reportUser(payload).subscribe({
      next: (res) => {
        console.log(res);
      },
      error: (err) => {
        console.error(err);
      },
    });
  }
}
