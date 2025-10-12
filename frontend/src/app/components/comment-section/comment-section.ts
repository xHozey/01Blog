import { Component, inject, Input, OnInit } from '@angular/core';
import { CommentService } from '../../service/comment-service';
import { commentResponse } from '../../models/commentResponse';
import { CommentComponent } from '../comment-component/comment-component';
import { FormsModule } from '@angular/forms';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { commentRequest } from '../../models/commentRequest';
import { UserService } from '../../service/user-service';
import { ReportModalComponent } from '../report-modal-component/report-modal-component';

@Component({
  selector: 'app-comment-section',
  templateUrl: './comment-section.html',
  styleUrls: ['./comment-section.css'],
  imports: [CommentComponent, FormsModule, InfiniteScrollDirective, ReportModalComponent],
})
export class CommentSection implements OnInit {
  commentService = inject(CommentService);
  userService = inject(UserService);

  @Input() postId!: number;
  comments: commentResponse[] = [];
  page: number = 0;
  loading: boolean = false;
  hasMore: boolean = true;
  user: userResponse | null = null;

  newCommentContent: string = '';
  selectedFile: File | null = null;

  ngOnInit(): void {
    this.loadComments();
    this.userService.user$.subscribe((user) => (this.user = user));
  }

  loadComments() {
    if (!this.postId || this.loading || !this.hasMore) {
      console.log('show ends !');
      return;
    }
    this.loading = true;
    this.commentService.fetchComments(this.postId, this.page).subscribe({
      next: (data) => {
        console.log(data);
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
    if (!this.postId || !this.newCommentContent) return;

    const payload: commentRequest = {
      postId: this.postId,
      content: this.newCommentContent,
    };

    this.commentService.addComment(payload).subscribe({
      next: (comment) => {
        this.comments.unshift(comment);
        this.newCommentContent = '';
        this.selectedFile = null;
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  onDelete(id: number) {
    this.commentService.deleteComment(id).subscribe({
      next: () => {
        this.comments = this.comments.filter((comment) => comment.id != id);
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  showReportModal = false;
  targetId = 0;
  onReport(id: number) {
    this.targetId = id;
    this.showReportModal = true;
  }
}
