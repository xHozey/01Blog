import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { AdminService } from '../../service/admin-service';
import { Router } from '@angular/router';
import { File, FileText, Flag, LucideAngularModule, User, UserX } from 'lucide-angular';
import { parseApiError } from '../../utils/errorHelper';
import { ToastService } from '../../service/toast-service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, NavbarComponent, LucideAngularModule],
  templateUrl: './admin-dashboard.html',
  styleUrls: ['./admin-dashboard.css'],
})
export class AdminDashboard implements OnInit {
  private adminService = inject(AdminService);
  private router = inject(Router);
  private toastService = inject(ToastService);

  activeTab: 'users' | 'userReports' | 'posts' | 'postReports' | 'bannedUsers' = 'users';
  isLoading = false;

  readonly UserIcon = User;
  readonly FlagIcon = Flag;
  readonly FileTextIcon = FileText;
  readonly BannedIcon = UserX;

  userReports: reportUser[] = [];
  posts: adminPostDTO[] = [];
  postReports: postReportDTO[] = [];
  users: adminUserDTO[] = [];
  bannedUsers: adminUserDTO[] = [];

  pageUsers = 0;
  userReportsPage = 0;
  pagePosts = 0;
  postReportsPage = 0;
  bannedUsersPage = 0;

  hasMoreUsers = true;
  hasMoreUserReports = true;
  hasMorePosts = true;
  hasMorePostReports = true;
  hasMoreBannedUsers = true;

  pageSize = 10;

  totalUsers = 0;
  totalUserReports = 0;
  totalPosts = 0;
  totalBan = 0;
  totalPostReport = 0;

  showConfirmModal = false;
  pendingAction: (() => void) | null = null;

  openConfirmModal(action: () => void) {
    this.pendingAction = action;
    this.showConfirmModal = true;
  }

  closeModal() {
    this.showConfirmModal = false;
    this.pendingAction = null;
  }

  confirmAction() {
    if (this.pendingAction) {
      this.pendingAction();
    }
    this.closeModal();
  }

  ngOnInit() {
    this.loadUsers();
    this.refreshStats();
  }

  refreshStats() {
    this.adminService.getTotalUsers().subscribe((n) => (this.totalUsers = n));
    this.adminService.getTotalUserReports().subscribe((n) => (this.totalUserReports = n));
    this.adminService.getTotalPosts().subscribe((n) => (this.totalPosts = n));
    this.adminService.getTotalBan().subscribe((n) => (this.totalBan = n));
    this.adminService.getTotalPostReports().subscribe((n) => (this.totalPostReport = n));
  }

  setTab(tab: 'users' | 'userReports' | 'posts' | 'postReports' | 'bannedUsers') {
    this.activeTab = tab;
    if (tab === 'users' && this.users.length === 0) this.loadUsers();
    if (tab === 'userReports' && this.userReports.length === 0) this.loadUserReports();
    if (tab === 'posts' && this.posts.length === 0) this.loadPosts();
    if (tab === 'postReports' && this.postReports.length === 0) this.loadPostReports();
    if (tab === 'bannedUsers' && this.bannedUsers.length === 0) this.loadBannedUsers();
  }

  loadBannedUsers() {
    this.isLoading = true;
    this.adminService.getBannedUsers(this.bannedUsersPage).subscribe({
      next: (res) => {
        this.bannedUsers = res;
        this.hasMoreBannedUsers = res.length === this.pageSize;
        this.isLoading = false;
      },
      error: () => (this.isLoading = false),
    });
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
    this.adminService.getUserReports(this.userReportsPage).subscribe({
      next: (res) => {
        this.userReports = res;
        if (this.pageSize === 0 && res.length > 0) this.pageSize = res.length;
        this.hasMoreUserReports = res.length === this.pageSize;
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
      error: (err) => {
        this.isLoading = false;
      },
    });
  }

  loadPostReports() {
    this.isLoading = true;
    this.adminService.getPostReports(this.postReportsPage).subscribe({
      next: (res) => {
        this.postReports = res;
        if (this.pageSize === 0 && res.length > 0) this.pageSize == res.length;
        this.hasMorePostReports = res.length === this.pageSize;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
      },
    });
  }

  toggleBan(user: adminUserDTO) {
    this.openConfirmModal(() => {
      this.adminService.toggleBanUser(user.id).subscribe({
        next: () => {
          if (this.activeTab == 'users') {
            user.isBanned = !user.isBanned;
          } else {
            this.bannedUsers = this.bannedUsers.filter((u) => u.id !== user.id);
            let findUser = this.users.find((u) => u.id == user.id);
            if (findUser) {
              findUser.isBanned = !findUser.isBanned;
            }
          }
          this.refreshStats();
        },
        error: (err) => {
          parseApiError(err).forEach((msg) => this.toastService.error(msg));
        },
      });
    });
  }

  deleteUser(user: adminUserDTO) {
    this.openConfirmModal(() => {
      this.adminService.deleteUser(user.id).subscribe({
        next: () => {
          this.users = this.users.filter((u) => u.id !== user.id);
          this.totalUsers--;
        },
      });
    });
  }

  deletePost(post: adminPostDTO) {
    this.openConfirmModal(() => {
      this.adminService.deletePost(post.id).subscribe({
        next: () => {
          this.posts = this.posts.filter((p) => p.id !== post.id);
          this.totalPosts--;
        },
      });
    });
  }

  hidePost(post: adminPostDTO) {
    this.openConfirmModal(() => {
      this.adminService.toggleHidePost(post.id).subscribe({
        next: () => (post.isHide = !post.isHide),
      });
    });
  }

  deleteUserReport(report: reportUser) {
    this.openConfirmModal(() => {
      this.adminService.deleteUserReport(report.id).subscribe({
        next: () => {
          this.userReports = this.userReports.filter((r) => r.id !== report.id);
          this.totalUserReports--;
        },
      });
    });
  }

  deletePostReport(report: postReportDTO) {
    this.openConfirmModal(() => {
      this.adminService.deletePostReport(report.id).subscribe({
        next: () => {
          this.postReports = this.postReports.filter((r) => r.id !== report.id);
          this.totalPostReport--;
        },
      });
    });
  }

  nextPostReportsPage() {
    if (this.hasMorePostReports) {
      this.postReportsPage++;
      this.loadPostReports();
    }
  }

  prevPostReportsPage() {
    if (this.postReportsPage > 0) {
      this.postReportsPage--;
      this.loadPostReports();
    }
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

  nextUserReportsPage() {
    if (this.hasMoreUserReports) {
      this.userReportsPage++;
      this.loadUserReports();
    }
  }

  prevUserReportsPage() {
    if (this.userReportsPage > 0) {
      this.userReportsPage--;
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

  nextBannedUsersPage() {
    if (this.hasMoreBannedUsers) {
      this.bannedUsersPage++;
      this.loadBannedUsers();
    }
  }

  prevBannedUsersPage() {
    if (this.bannedUsersPage > 0) {
      this.bannedUsersPage--;
      this.loadBannedUsers();
    }
  }

  goToPost(id: number) {
    this.router.navigate(['/post', id]);
  }

  goToUser(id: number) {
    this.router.navigate(['/profile', id]);
  }
}
