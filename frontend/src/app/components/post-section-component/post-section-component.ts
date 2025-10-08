import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostComponent } from '../post-component/post-component';
import { PostService } from '../../service/post-service';
import { FormsModule } from '@angular/forms';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-section',
  standalone: true,
  imports: [CommonModule, PostComponent, FormsModule, InfiniteScrollDirective],
  templateUrl: './post-section-component.html',
})
export class PostSectionComponent implements OnInit {
  posts: postResponse[] = [];
  page: number = 0;

  //Post report form
  selectedReportPostId?: number;
  showReportModal = false;
  reportDescription: string = '';

  private postService = inject(PostService);
  private router = inject(Router);

  ngOnInit(): void {
    this.postService.getPosts(this.page).subscribe({
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
    this.postService.getPosts(this.page).subscribe({
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
