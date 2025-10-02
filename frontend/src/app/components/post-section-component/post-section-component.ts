import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostComponent } from '../post-component/post-component';
import { PostService } from '../../service/post-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-post-section',
  standalone: true,
  imports: [CommonModule, PostComponent, FormsModule],
  templateUrl: './post-section-component.html',
})
export class PostSectionComponent implements OnInit {
  posts: postResponse[] = [];

  constructor(private postService: PostService) {}

  selectedPostId?: number;
  showUpdateModal = false;
  updateForm: { title: string; content: string; image?: File; video?: File } = {
    title: '',
    content: '',
  };

  onUpdatePost(postId: number) {
    this.selectedPostId = postId;
    const post = this.posts.find((p) => p.id === postId);
    if (!post) return;

    // prefill form
    this.updateForm.title = post.title;
    this.updateForm.content = post.content;

    this.showUpdateModal = true;
  }

  onFileChange(event: Event, type: 'image' | 'video') {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.updateForm[type] = input.files[0];
    }
  }

  saveUpdate() {
    if (!this.selectedPostId) return;

    const formData = new FormData();
    formData.append('id', this.selectedPostId.toString());
    formData.append('title', this.updateForm.title);
    formData.append('content', this.updateForm.content);
    if (this.updateForm.image) formData.append('image', this.updateForm.image);
    if (this.updateForm.video) formData.append('video', this.updateForm.video);

    this.postService.updatePost(formData).subscribe({
      next: (updatedPost) => {
        const index = this.posts.findIndex((p) => p.id === updatedPost.id);
        if (index > -1) this.posts[index] = updatedPost;
        this.showUpdateModal = false;
        this.selectedPostId = undefined;
        this.updateForm = { title: '', content: '' };
      },
      error: (err) => console.error(err),
    });
  }

  onHidePost(postId: number) {
    throw new Error('Method not implemented.');
  }
  onReportPost(postId: number) {
    throw new Error('Method not implemented.');
  }
  onDeletePost(postId: number) {
    this.postService.deletePost(postId).subscribe({
      next: () => {
        this.posts = this.posts.filter((post) => post.id !== postId);
      },
      error: (err) => console.error(err),
    });
  }

  ngOnInit() {
    this.fetchPosts();
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
