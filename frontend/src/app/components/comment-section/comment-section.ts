import { Component, inject, Input, OnInit } from '@angular/core';
import { CommentService } from '../../service/comment-service';
import { CommentComponent } from '../comment-component/comment-component';
import { FormsModule } from '@angular/forms';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { UserService } from '../../service/user-service';
import { ReportModalComponent } from '../report-modal-component/report-modal-component';
import { parseApiError } from '../../utils/errorHelper';
import { ToastService } from '../../service/toast-service';

@Component({
  selector: 'app-comment-section',
  templateUrl: './comment-section.html',
  styleUrls: ['./comment-section.css'],
  imports: [CommentComponent, FormsModule, InfiniteScrollDirective, ReportModalComponent],
})
export class CommentSection implements OnInit {
  commentService = inject(CommentService);
  private userService = inject(UserService);
  private toastService = inject(ToastService);
  @Input() postId!: number;
  comments: commentResponse[] = [];
  page: number = 0;
  loading: boolean = false;
  hasMore: boolean = true;
  user: userResponse | null = null;

  newCommentContent: string = '';

  ngOnInit(): void {
    this.loadComments();
    this.userService.user$.subscribe((user) => (this.user = user));
  }

  loadComments() {
    if (!this.postId || this.loading || !this.hasMore) {
      return;
    }
    this.loading = true;
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
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
        this.loading = false;
      },
    });
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
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  onDelete(id: number) {
    this.commentService.deleteComment(id).subscribe({
      next: () => {
        this.comments = this.comments.filter((comment) => comment.id != id);
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
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
