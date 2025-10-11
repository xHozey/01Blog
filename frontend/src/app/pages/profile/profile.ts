import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../service/user-service';
import { FollowService } from '../../service/follow-service';
import { ActivatedRoute, Router } from '@angular/router';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { CommonModule } from '@angular/common';
import { UserPosts } from '../../components/user-posts/user-posts';

@Component({
  selector: 'app-profile',
  imports: [NavbarComponent, CommonModule, UserPosts],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  private userService = inject(UserService);
  private followService = inject(FollowService);
  private router = inject(Router);
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
        console.log(res);
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
}
