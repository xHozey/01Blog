import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Heart, LucideAngularModule } from "lucide-angular"; // <-- import this
@Component({
  selector: 'app-post-component',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './post-component.html',
  styleUrls: ['./post-component.css'],
})
export class PostComponent implements OnInit {
  @Input() post!: postResponse;
  readonly HeartIcon = Heart
  showComments = false;
  liked = false; // initialize safely

  ngOnInit() {
    if (this.post) {
      this.liked = !!this.post.isLiked; // set after @Input is ready
    }
  }

  toggleLike() {
    this.liked = !this.liked;
    // Optionally, update post.likes
    if (this.liked) {
      this.post.likes++;
    } else {
      this.post.likes--;
    }
  }

  toggleComments() {
    this.showComments = !this.showComments;
  }
}
