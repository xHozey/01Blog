import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Ellipsis, Heart, LucideAngularModule } from 'lucide-angular'; // <-- import this
import { EngagementService } from '../../service/engagement-service';
import { UserService } from '../../service/user-service';
import { CommentSection } from "../comment-section/comment-section";

@Component({
  selector: 'app-post-component',
  standalone: true,
  imports: [CommonModule, LucideAngularModule, CommentSection],
  templateUrl: './post-component.html',
  styleUrls: ['./post-component.css'],
})
export class PostComponent implements OnInit {
  constructor(private engagementService: EngagementService, private userService: UserService) {}

  @Input() post!: postResponse;

  @Output() delete = new EventEmitter<number>();
  @Output() update = new EventEmitter<number>();
  @Output() hide = new EventEmitter<number>();
  @Output() report = new EventEmitter<number>();

  readonly HeartIcon = Heart;
  readonly EllipsisIcon = Ellipsis;
  showComments = false;
  liked = false;
  user: userResponse | null = null;

  getFileType(filePath: string): 'image' | 'video' | 'unknown' {
    const ext = filePath.split('.').pop()?.toLowerCase();
    if (!ext) return 'unknown';

    const imageExts = ['jpg', 'jpeg', 'png', 'gif', 'webp'];
    const videoExts = ['mp4', 'webm', 'ogg'];

    if (imageExts.includes(ext)) return 'image';
    if (videoExts.includes(ext)) return 'video';
    return 'unknown';
  }

  ngOnInit() {
    if (this.post) {
      this.liked = this.post.isLiked;
    }
    this.userService.user$.subscribe((user) => (this.user = user));
  }

  toggleLike() {
    this.engagementService.likePost(this.post.id).subscribe({
      next: (res) => {
        this.liked = !this.liked;
        if (this.liked) {
          this.post.likes++;
        } else {
          this.post.likes--;
        }
        console.log(res);
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  toggleComments() {
    this.showComments = !this.showComments;
  }
}
