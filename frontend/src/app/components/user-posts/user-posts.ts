import { Component, inject, Input, OnInit } from '@angular/core';
import { PostService } from '../../service/post-service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PostComponent } from '../post-component/post-component';
import { FormsModule } from '@angular/forms';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';

@Component({
  selector: 'app-user-posts',
  imports: [CommonModule, PostComponent, FormsModule, InfiniteScrollDirective],
  templateUrl: './user-posts.html',
  styleUrl: './user-posts.css',
})
export class UserPosts implements OnInit {
  @Input() userId!: number;

  posts: postResponse[] = [];
  page: number = 0;

  //Post report form
  selectedReportPostId?: number;
  showReportModal = false;
  reportDescription: string = '';

  private postService = inject(PostService);
  private router = inject(Router);

  ngOnInit(): void {
    this.postService.getUserPosts(this.userId, this.page).subscribe({
      next: (data) => (this.posts = data),
      error: (err) => console.error(err),
    });
  }
  onDeletePost(postId: number) {
    this.postService.deletePost(postId).subscribe({
      next: () => (this.posts = this.posts.filter((post) => post.id !== postId)),
      error: (err) => console.error(err),
    });
  }

  onReportPost(postId: number) {
    this.showReportModal = true;
    this.selectedReportPostId = postId;
    this.reportDescription = '';
  }

  saveReport() {
    if (!this.selectedReportPostId || !this.reportDescription.trim()) return;
    const payload: reportRequest = {
      postId: this.selectedReportPostId,
      description: this.reportDescription,
    };

    this.postService.reportPost(payload).subscribe({
      next: (res) => {
        this.resetReportForm();
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  resetReportForm() {
    this.showReportModal = false;
    this.selectedReportPostId = undefined;
    this.reportDescription = '';
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
        console.error(err);
      },
    });
  }

  onUpdatePost(id: number) {
    this.router.navigate(['/edit', id]);
  }
}
