import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Heart, LucideAngularModule } from 'lucide-angular'; // <-- import this
import { EngagementService } from '../../service/engagement-service';
@Component({
  selector: 'app-post-component',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './post-component.html',
  styleUrls: ['./post-component.css'],
})
export class PostComponent implements OnInit {
  @Input() post!: postResponse;
  readonly HeartIcon = Heart;
  showComments = false;
  liked = false;

  constructor(private engagementService: EngagementService) {}

  ngOnInit() {
    if (this.post) {
      console.log(this.post)
      this.liked = this.post.isLiked;
    }
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
