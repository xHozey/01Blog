import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostComponent } from '../post-component/post-component';
import { PostService } from '../../service/post-service';
import { FormsModule } from '@angular/forms';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { Router } from '@angular/router';
import { ReportModalComponent } from '../report-modal-component/report-modal-component';
import { UserService } from '../../service/user-service';

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

  private postService = inject(PostService);
  private router = inject(Router);
  private userService = inject(UserService);

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

  showReportModal = false;
  reportDescription = '';
  targetId = 0;
  
  onReport(id: number) {
    this.targetId = id;
    this.showReportModal = true;
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
