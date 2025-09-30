import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostComponent } from '../post-component/post-component';
import { PostService } from '../../service/post-service';

@Component({
  selector: 'app-post-section',
  standalone: true,
  imports: [CommonModule, PostComponent],
  templateUrl: './post-section-component.html'
})
export class PostSectionComponent implements OnInit {
  posts: postResponse[] = [];
  constructor (private postService: PostService) {}
  ngOnInit() {
    this.fetchPosts()
  }

  fetchPosts() {
    this.postService.getPosts(0).subscribe({
      next: (data) => {
        this.posts.push(...data)
      },
      error: (err) => {console.error(err)}
    });
    console.log(this.posts)
  }
}
