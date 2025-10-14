import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import DOMPurify from 'dompurify';
import { PostService } from '../../service/post-service';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { EngagementService } from '../../service/engagement-service';
import { CommonModule } from '@angular/common';
import { CommentSection } from '../../components/comment-section/comment-section';
import { Ellipsis, Heart, LucideAngularModule } from 'lucide-angular';
import { ToastService } from '../../service/toast-service';
import { parseApiError } from '../../utils/errorHelper';
import { AdminService } from '../../service/admin-service';
import { UserService } from '../../service/user-service';
import { ReportModalComponent } from '../../components/report-modal-component/report-modal-component';

@Component({
  selector: 'app-post',
  imports: [
    NavbarComponent,
    CommonModule,
    CommentSection,
    LucideAngularModule,
    ReportModalComponent,
  ],
  templateUrl: './post.html',
  styleUrls: ['./post.css'],
})
export class Post implements OnInit {
  readonly HeartIcon = Heart;
  readonly EllipsisIcon = Ellipsis;
  private route = inject(ActivatedRoute);
  private postService = inject(PostService);
  private sanitizer = inject(DomSanitizer);
  private engagementSerivce = inject(EngagementService);
  private toastService = inject(ToastService);
  private adminService = inject(AdminService);
  private router = inject(Router);
  private userService = inject(UserService);

  user: userResponse | null = null;
  postId!: number;
  post!: postResponse;
  safeContent!: SafeHtml;
  isLiked: boolean = false;
  showComments: boolean = false;

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.postId = idParam ? +idParam : 0;
    this.userService.user$.subscribe((user) => (this.user = user));
    this.postService.getPost(this.postId).subscribe({
      next: (res) => {
        this.post = res;
        this.isLiked = this.post.isLiked;
        this.safeContent = this.sanitizeContent(this.post.content);
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  sanitizeContent(content: string): SafeHtml {
    const cleanHtml = DOMPurify.sanitize(content);
    return this.sanitizer.bypassSecurityTrustHtml(cleanHtml);
  }

  toggleLike() {
    this.engagementSerivce.likePost(this.postId).subscribe({
      next: () => {
        this.isLiked = !this.isLiked;
        if (this.isLiked) this.post.likes++;
        else this.post.likes--;
      },
      error: (err) => parseApiError(err).forEach((msg) => this.toastService.error(msg)),
    });
  }

  toggleComments() {
    this.showComments = !this.showComments;
  }

  showReportModal = false;
  targetId = 0;

  onReport(id: number) {
    this.targetId = id;
    this.showReportModal = true;
  }

  onHide(id: number) {
    this.adminService.toggleHidePost(id).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  onDelete(postId: number) {
    this.postService.deletePost(postId).subscribe({
      next: () => this.router.navigate(['/']),
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }

  onUpdate(id: number) {
    this.router.navigate(['/edit', id]);
  }

  goToProfile() {
    this.router.navigate(["/profile", this.post.authorId])
  }
}
