import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostComponent } from '../post-component/post-component';

@Component({
  selector: 'app-post-section',
  standalone: true,
  imports: [CommonModule, PostComponent],
  templateUrl: './post-section-component.html'
})
export class PostSectionComponent {
  posts: postResponse[] = [
    {
      id: 1,
      title: 'Welcome to the feed!',
      content: 'This is your first post 🎉',
      imagePath: '',
      videoPath: '',
      date: new Date().toISOString(),
      likes: 0,
      liked: false
    }
  ];
}
