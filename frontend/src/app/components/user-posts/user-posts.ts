import { Component, inject, Input, OnInit } from '@angular/core';
import { PostService } from '../../service/post-service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PostComponent } from '../post-component/post-component';
import { FormsModule } from '@angular/forms';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { ReportModalComponent } from '../report-modal-component/report-modal-component';
import { ToastService } from '../../service/toast-service';
import { parseApiError } from '../../utils/errorHelper';

@Component({
  selector: 'app-user-posts',
  imports: [
    CommonModule,
    PostComponent,
    FormsModule,
    InfiniteScrollDirective,
    ReportModalComponent,
  ],
  templateUrl: './user-posts.html',
  styleUrl: './user-posts.css',
})
export class UserPosts implements OnInit {
  @Input() userId!: number;

  posts: postResponse[] = [];
  page: number = 0;

  selectedReportId?: number;
  showReportModal = false;
  private toastService = inject(ToastService);
  private postService = inject(PostService);
  private router = inject(Router);

  ngOnInit(): void {
    this.postService.getUserPosts(this.userId, this.page).subscribe({
      next: (data) => (this.posts = data),
      error: (err) => parseApiError(err).forEach((msg) => this.toastService.error(msg)),
    });
  }

  onDeletePost(postId: number) {
    this.postService.deletePost(postId).subscribe({
      next: () => (this.posts = this.posts.filter((post) => post.id !== postId)),
      error: (err) => parseApiError(err).forEach((msg) => this.toastService.error(msg)),
    });
  }

  onReportPost(id: number) {
    this.showReportModal = true;
    this.selectedReportId = id;
  }

  loadMore() {
    this.page++;
    this.postService.getUserPosts(this.userId, this.page).subscribe({
      next: (data) => {
        if (data) {
          this.posts = [...this.posts, ...data];
        } else {
          console.log('no more!');
        }
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  onUpdatePost(id: number) {
    this.router.navigate(['/edit', id]);
  }
}
