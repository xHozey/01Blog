import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { commentResponse } from '../../models/commentResponse';
import { CommonModule } from '@angular/common';
import { UserService } from '../../service/user-service';
import { EngagementService } from '../../service/engagement-service';
import { Ellipsis, Heart, LucideAngularModule } from 'lucide-angular';

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

  userService = inject(UserService);
  engagementService = inject(EngagementService);
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
        console.error(err);
      },
    });
  }

  ngOnInit(): void {
    this.userService.user$.subscribe((user) => (this.user = user));
  }
}
