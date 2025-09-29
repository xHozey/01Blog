import { Component } from '@angular/core';
import { PostComponent } from '../post-component/post-component';

@Component({
  selector: 'app-post-section-component',
  imports: [PostComponent],
  templateUrl: './post-section-component.html',
  styleUrl: './post-section-component.css',
})
export class PostSectionComponent {
  posts: postResponse[] = [
    {
      id: 1,
      title: 'My First Post',
      content: 'This is a text post!',
      videoPath: '',
      imagePath: 'https://duckduckgo.com/?q=test&t=newext&atb=v501-4&ia=images&iax=images&iai=https%3A%2F%2Fgenius-u-attachments.s3.amazonaws.com%2Fuploads%2Farticle%2Fimage%2F3874029%2FHow_Psychometric_tests_are_helpful_in_determining_a_candidate_s_role_and_performance.jpg',
      date: new Date().toISOString(),
    },
    {
      id: 2,
      title: 'Another Post',
      content: 'Here is a video post',
      videoPath: 'https://www.youtube.com/watch?v=C6EHlfn6kv0',
      imagePath: '',
      date: new Date().toISOString(),
    },
  ];
}
