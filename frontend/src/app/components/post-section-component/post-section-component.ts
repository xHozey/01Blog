import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostComponent } from '../post-component/post-component';
import { PostService } from '../../service/post-service';
import { FormsModule } from '@angular/forms';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { Router } from '@angular/router';
import { ReportModalComponent } from '../report-modal-component/report-modal-component';
import { parseApiError } from '../../utils/errorHelper';
import { ToastService } from '../../service/toast-service';
import { AdminService } from '../../service/admin-service';

@Component({
  selector: 'app-post-section',
  standalone: true,
  imports: [
    CommonModule,
    PostComponent,
    FormsModule,
    InfiniteScrollDirective,
    ReportModalComponent,
  ],
  templateUrl: './post-section-component.html',
})
export class PostSectionComponent implements OnInit {
  posts: postResponse[] = [];
  page: number = 0;
  private toastService = inject(ToastService);
  private postService = inject(PostService);
  private router = inject(Router);
  private adminService = inject(AdminService);

  ngOnInit(): void {
    this.postService.getPosts(this.page).subscribe({
      next: (data) => (this.posts = data),
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  showReportModal = false;
  postId = 0;
  userID = 0;
  onReport(id: number) {
    this.postId = id;
    const reportedPost = this.posts.find(p => p.id == this.postId)
    if (reportedPost) this.userID = reportedPost.authorId
    this.showReportModal = true;
  }

  onHide(id: number) {
    this.adminService.toggleHidePost(id).subscribe({
      next: () => {
        this.posts = this.posts.filter((p) => p.id != id);
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  onDeletePost(postId: number) {
    this.postService.deletePost(postId).subscribe({
      next: () => (this.posts = this.posts.filter((post) => post.id !== postId)),
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  onUpdatePost(id: number) {
    this.router.navigate(['/edit', id]);
  }

  loadMore() {
    this.page++;
    this.postService.getPosts(this.page).subscribe({
      next: (data) => {
        if (data) {
          this.posts = [...this.posts, ...data];
        }
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }
}
