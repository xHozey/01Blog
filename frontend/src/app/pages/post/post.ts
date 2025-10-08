import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import DOMPurify from 'dompurify';
import { PostService } from '../../service/post-service';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { EngagementService } from '../../service/engagement-service';
import { CommonModule } from '@angular/common';
import { CommentSection } from '../../components/comment-section/comment-section';
import { Heart, LucideAngularModule, LucideIconProvider } from 'lucide-angular';

interface postResponse {
  id: number;
  title: string;
  content: string;
  author: string;
  authorId: number;
  createdAt: string;
  likes: number;
  isLiked: boolean;
}

@Component({
  selector: 'app-post',
  imports: [NavbarComponent, CommonModule,CommentSection, LucideAngularModule],
  templateUrl: './post.html',
  styleUrls: ['./post.css'],
})
export class Post implements OnInit {
  readonly HeartIcon = Heart;

  private route = inject(ActivatedRoute);
  private postService = inject(PostService);
  private sanitizer = inject(DomSanitizer);
  private engagementSerivce = inject(EngagementService);
  postId!: number;
  post!: postResponse;
  safeContent!: SafeHtml;
  isLiked: boolean = false;
  showComments: boolean = false;

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.postId = idParam ? +idParam : 0;

    this.postService.getPost(this.postId).subscribe({
      next: (res) => {
        this.post = res;
        this.isLiked = this.post.isLiked;
        this.safeContent = this.sanitizeContent(this.post.content);
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  sanitizeContent(content: string): SafeHtml {
    const cleanHtml = DOMPurify.sanitize(content);
    return this.sanitizer.bypassSecurityTrustHtml(cleanHtml);
  }

  toggleLike() {
    this.engagementSerivce.likePost(this.postId).subscribe({
      next: (res) => {
        this.isLiked = !this.isLiked;
        if (this.isLiked) this.post.likes++;
        else this.post.likes--;
      },
      error: (err) => console.error(err),
    });
  }

  toggleComments() {
    this.showComments = !this.showComments;
  }
}
