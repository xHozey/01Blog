import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostComponent } from '../post-component/post-component';
import { PostService } from '../../service/post-service';
import { PostCreationComponent } from '../post-creation-component/post-creation-component';
import { UserService } from '../../service/user-service';

@Component({
  selector: 'app-post-section',
  standalone: true,
  imports: [CommonModule, PostComponent, PostCreationComponent],
  templateUrl: './post-section-component.html',
})
export class PostSectionComponent implements OnInit {
  posts: postResponse[] = [];
  user: userResponse | null = null;

  addPost(post: postResponse) {
    this.posts.unshift(post);
  }

  constructor(private postService: PostService, private userSerivce: UserService) {}

  ngOnInit() {
    this.fetchPosts();
    this.userSerivce.user$.subscribe((user) => (this.user = user));
  }

  fetchPosts() {
    this.postService.getPosts(0).subscribe({
      next: (data) => {
        this.posts.push(...data);
      },
      error: (err) => {
        console.error(err);
      },
    });
    console.log(this.posts);
  }
}
