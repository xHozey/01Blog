import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Ellipsis, Heart, LucideAngularModule } from 'lucide-angular'; // <-- import this
import { EngagementService } from '../../service/engagement-service';
import { UserService } from '../../service/user-service';
import { CommentSection } from '../comment-section/comment-section';
import { Router } from '@angular/router';
import { DomSanitizer } from '@angular/platform-browser';
import DOMPurify from 'dompurify';

@Component({
  selector: 'app-post-component',
  standalone: true,
  imports: [CommonModule, LucideAngularModule, CommentSection],
  templateUrl: './post-component.html',
  styleUrls: ['./post-component.css'],
})
export class PostComponent implements OnInit {
  private engagementService: EngagementService = inject(EngagementService);
  private userService: UserService = inject(UserService);
  private sanitizer: DomSanitizer = inject(DomSanitizer)
  private router: Router = inject(Router)
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

  getSnippet(content: string) {
    // Sanitize content
    const cleanContent = DOMPurify.sanitize(content);
    console.log(cleanContent)
    // Truncate safely
    const snippet = cleanContent.length > 400
      ? cleanContent.slice(0, 400) + '...'
      : cleanContent;

    return this.sanitizer.bypassSecurityTrustHtml(snippet);
  }

  goToPost() {
  this.router.navigate(['/post', this.post.id]);
}
}
