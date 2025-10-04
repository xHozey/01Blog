import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { commentResponse } from '../../models/commentResponse';
import { CommonModule } from '@angular/common';
import { UserService } from '../../service/user-service';

@Component({
  selector: 'app-comment-component',
  imports: [CommonModule],
  templateUrl: './comment-component.html',
  styleUrl: './comment-component.css',
})
export class CommentComponent implements OnInit {
  @Input() comment!: commentResponse;

  @Output() delete = new EventEmitter<number>();
  @Output() hide = new EventEmitter<number>();
  @Output() report = new EventEmitter<number>();

  userService = inject(UserService);
  user: userResponse | null = null;

  toggleLike() {
    this.comment.isLiked = !this.comment.isLiked;
    this.comment.likes += this.comment.isLiked ? 1 : -1;
    // TODO: call CommentService to update like state
  }

  ngOnInit(): void {
    this.userService.user$.subscribe((user) => (this.user = user));
  }
}
