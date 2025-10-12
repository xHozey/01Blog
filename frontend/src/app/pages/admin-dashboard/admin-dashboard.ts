import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { AdminService } from '../../service/admin-service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, NavbarComponent],
  templateUrl: './admin-dashboard.html',
  styleUrls: ['./admin-dashboard.css'],
})
export class AdminDashboard implements OnInit {
  private adminService = inject(AdminService);

  activeTab: 'users' | 'userReports' | 'posts' = 'users';
  isLoading = false;

  users: adminUserDTO[] = [];
  userReports: reportUser[] = [];
  posts: adminPostDTO[] = [];

  pageUsers = 0;
  pageReports = 0;
  pagePosts = 0;

  hasMoreUsers = true;
  hasMoreReports = true;
  hasMorePosts = true;

  pageSize = 10;

  totalUsers = 0;
  totalReports = 0;
  totalPosts = 0;

  ngOnInit() {
    this.loadUsers();
    this.refreshStats();
  }

  refreshStats() {
    this.adminService.getTotalUsers().subscribe((n) => (this.totalUsers = n));
    this.adminService.getTotalReports().subscribe((n) => (this.totalReports = n));
    this.adminService.getTotalPosts().subscribe((n) => (this.totalPosts = n));
  }

  setTab(tab: 'users' | 'userReports' | 'posts') {
    this.activeTab = tab;
    if (tab === 'users' && this.users.length === 0) this.loadUsers();
    if (tab === 'userReports' && this.userReports.length === 0) this.loadUserReports();
    if (tab === 'posts' && this.posts.length === 0) this.loadPosts();
  }

  loadUsers() {
    this.isLoading = true;
    this.adminService.getUsers(this.pageUsers).subscribe({
      next: (res) => {
        this.users = res;
        if (this.pageSize === 0 && res.length > 0) this.pageSize = res.length;
        this.hasMoreUsers = res.length === this.pageSize;
        this.isLoading = false;
      },
      error: () => (this.isLoading = false),
    });
  }

  loadUserReports() {
    this.isLoading = true;
    this.adminService.getUserReports(this.pageReports).subscribe({
      next: (res) => {
        this.userReports = res;
        if (this.pageSize === 0 && res.length > 0) this.pageSize = res.length;
        this.hasMoreReports = res.length === this.pageSize;
        this.isLoading = false;
      },
      error: () => (this.isLoading = false),
    });
  }

  loadPosts() {
    this.isLoading = true;
    this.adminService.getPosts(this.pagePosts).subscribe({
      next: (res) => {
        this.posts = res;
        if (this.pageSize === 0 && res.length > 0) this.pageSize = res.length;
        this.hasMorePosts = res.length === this.pageSize;
        this.isLoading = false;
      },
      error: () => (this.isLoading = false),
    });
  }

  toggleBan(user: adminUserDTO) {
    this.adminService.toggleBanUser(user.id).subscribe({
      next: () => (user.isBanned = !user.isBanned),
    });
  }

  deleteUser(user: adminUserDTO) {
    if (!confirm(`Delete user "${user.username}" permanently?`)) return;
    this.adminService.deleteUser(user.id).subscribe({
      next: () => {
        this.users = this.users.filter((u) => u.id !== user.id);
        this.totalUsers--;
      },
    });
  }

  deleteReport(report: reportUser) {
    // if (!confirm(`Delete report?`)) return;
    // this.adminService.deleteReport(report.id).subscribe({
    //   next: () => {
    //     this.userReports = this.userReports.filter((r) => r.id !== report.id);
    //     this.totalReports--;
    //   },
    // });
  }

  deletePost(post: adminPostDTO) {
    if (!confirm(`Delete post "${post.title}" permanently?`)) return;
    this.adminService.deletePost(post.id).subscribe({
      next: () => {
        this.posts = this.posts.filter((p) => p.id !== post.id);
        this.totalPosts--;
      },
    });
  }

  hidePost(post: adminPostDTO) {
    this.adminService.toggleHidePost(post.id).subscribe({
      next: () => (post.isHide = !post.isHide),
    });
  }

  nextUsersPage() {
    if (this.hasMoreUsers) {
      this.pageUsers++;
      this.loadUsers();
    }
  }

  prevUsersPage() {
    if (this.pageUsers > 0) {
      this.pageUsers--;
      this.loadUsers();
    }
  }

  nextReportsPage() {
    if (this.hasMoreReports) {
      this.pageReports++;
      this.loadUserReports();
    }
  }

  prevReportsPage() {
    if (this.pageReports > 0) {
      this.pageReports--;
      this.loadUserReports();
    }
  }

  nextPostsPage() {
    if (this.hasMorePosts) {
      this.pagePosts++;
      this.loadPosts();
    }
  }

  prevPostsPage() {
    if (this.pagePosts > 0) {
      this.pagePosts--;
      this.loadPosts();
    }
  }
}
