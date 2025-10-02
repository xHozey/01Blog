import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Heart, LucideAngularModule } from 'lucide-angular'; // <-- import this
import { EngagementService } from '../../service/engagement-service';
import { UserService } from '../../service/user-service';
import { PostService } from '../../service/post-service';
@Component({
  selector: 'app-post-component',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
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
  showComments = false;
  liked = false;
  user: userResponse | null = null;



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
