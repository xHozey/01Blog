import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../service/user-service';
import { EngagementService } from '../../service/engagement-service';
import { Ellipsis, Heart, LucideAngularModule } from 'lucide-angular';
import { Router } from '@angular/router';
import { ToastService } from '../../service/toast-service';
import { parseApiError } from '../../utils/errorHelper';

@Component({
  selector: 'app-comment-component',
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './comment-component.html',
  styleUrl: './comment-component.css',
})
export class CommentComponent implements OnInit {
  @Input() comment!: commentResponse;

  @Output() delete = new EventEmitter<number>();
  @Output() report = new EventEmitter<number>();

  private toastService = inject(ToastService);
  private userService = inject(UserService);
  private engagementService = inject(EngagementService);
  private router = inject(Router);

  user: userResponse | null = null;

  readonly HeartIcon = Heart;
  readonly EllipsisIcon = Ellipsis;

  toggleLike() {
    this.engagementService.likeComment(this.comment.id).subscribe({
      next: () => {
        this.comment.isLiked = !this.comment.isLiked;
        this.comment.likes += this.comment.isLiked ? 1 : -1;
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  ngOnInit(): void {
    this.userService.user$.subscribe((user) => (this.user = user));
  }

  goToProfile(): void {
    this.router.navigate(['/profile', this.comment.authorId]);
  }
}
