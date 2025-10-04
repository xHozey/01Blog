import { Component, inject, Input, OnInit } from '@angular/core';
import { CommentService } from '../../service/comment-service';
import { commentResponse } from '../../models/commentResponse';
import { CommentComponent } from '../comment-component/comment-component';
import { FormsModule } from '@angular/forms';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';

@Component({
  selector: 'app-comment-section',
  templateUrl: './comment-section.html',
  styleUrls: ['./comment-section.css'],
  imports: [CommentComponent, FormsModule, InfiniteScrollDirective],
})
export class CommentSection implements OnInit {
  commentService = inject(CommentService);

  @Input() postId!: number;
  comments: commentResponse[] = [];
  page: number = 0;
  loading: boolean = false;
  hasMore: boolean = true;

  newCommentContent: string = '';
  selectedFile: File | null = null;

  ngOnInit(): void {
    this.loadComments();
  }

  loadComments() {
    if (!this.postId || this.loading || !this.hasMore) {
      console.log('show ends !');
      return;
    }
    this.loading = true;
    console.log('show still work :) current page is; ', this.page);
    this.commentService.fetchComments(this.postId, this.page).subscribe({
      next: (data) => {
        if (data.length === 0) {
          this.hasMore = false;
        } else {
          this.comments = [...this.comments, ...data];
          this.page++;
        }
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
      },
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  submitComment() {
    if (!this.postId || (!this.newCommentContent && !this.selectedFile)) return;

    const formData = new FormData();
    formData.append('postId', this.postId.toString());
    formData.append('content', this.newCommentContent);
    if (this.selectedFile) {
      formData.append('file', this.selectedFile);
    }

    this.commentService.addComment(formData).subscribe({
      next: (comment) => {
        this.comments.unshift(comment); // show new comment immediately
        this.newCommentContent = '';
        this.selectedFile = null;
      },
      error: (err) => {
        console.error(err);
      },
    });
  }
}
