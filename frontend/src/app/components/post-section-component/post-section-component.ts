import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostComponent } from '../post-component/post-component';
import { PostService } from '../../service/post-service';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../service/user-service';

@Component({
  selector: 'app-post-section',
  standalone: true,
  imports: [CommonModule, PostComponent, FormsModule],
  templateUrl: './post-section-component.html',
})
export class PostSectionComponent implements OnInit {
  posts: postResponse[] = [];

  //Post update form
  selectedPostId?: number;
  showUpdateModal = false;
  updateForm = { title: '', content: '' };
  selectedFile: File | null = null;
  previewUrl: string | null = null;
  dragOver = false;

  //Post report form
  selectedReportPostId?: number;
  showReportModal = false;
  reportDescription: string = '';

  constructor(private postService: PostService) {}

  onUpdatePost(postId: number) {
    this.selectedPostId = postId;
    const post = this.posts.find((p) => p.id === postId);
    if (!post) return;
    this.updateForm.title = post.title;
    this.updateForm.content = post.content;
    this.showUpdateModal = true;
  }

  onDeletePost(postId: number) {
    this.postService.deletePost(postId).subscribe({
      next: () => (this.posts = this.posts.filter((post) => post.id !== postId)),
      error: (err) => console.error(err),
    });
  }

  onReportPost(postId: number) {
    this.showReportModal = true;
    this.selectedReportPostId = postId;
    this.reportDescription = '';
  }

  saveReport() {
    if (!this.selectedReportPostId || !this.reportDescription.trim()) return;
    const payload: reportRequest = {
      postId: this.selectedReportPostId,
      description: this.reportDescription,
    };

    console.log(payload);
    this.postService.reportPost(payload).subscribe({
      next: (res) => {
        console.log(res);
        this.resetReportForm();
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  resetReportForm() {
    this.showReportModal = false;
    this.selectedReportPostId = undefined;
    this.reportDescription = '';
  }

  onFileSelect(event: Event) {
    console.log('File selected', event);
    const input = event.target as HTMLInputElement;
    if (input.files?.length) this.handleFile(input.files[0]);
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
    this.dragOver = true;
  }

  onDragLeave() {
    this.dragOver = false;
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    this.dragOver = false;
    const files = event.dataTransfer?.files;
    if (files?.length) this.handleFile(files[0]);
  }

  private handleFile(file: File) {
    if (!file.type.startsWith('image/') && !file.type.startsWith('video/')) return;
    this.selectedFile = file;
    const reader = new FileReader();
    reader.onload = () => (this.previewUrl = reader.result as string);
    reader.readAsDataURL(file);
  }

  get isFileImage(): boolean {
    return !!this.selectedFile?.type.startsWith('image/');
  }

  get isFileVideo(): boolean {
    return !!this.selectedFile?.type.startsWith('video/');
  }

  removeFile(fileInput: HTMLInputElement) {
    this.selectedFile = null;
    this.previewUrl = null;
    fileInput.value = '';
  }

  saveUpdate(fileInput: HTMLInputElement) {
    if (!this.selectedPostId) return;

    const formData = new FormData();
    formData.append('id', this.selectedPostId.toString());
    formData.append('title', this.updateForm.title);
    formData.append('content', this.updateForm.content);

    if (this.selectedFile) {
      formData.append('file', this.selectedFile);
    }

    this.postService.updatePost(formData).subscribe({
      next: (updatedPost) => {
        const index = this.posts.findIndex((p) => p.id === updatedPost.id);
        if (index > -1) this.posts[index] = updatedPost;
        this.resetUpdateForm(fileInput);
      },
      error: (err) => console.error(err),
    });
  }

  resetUpdateForm(fileInput: HTMLInputElement) {
    this.showUpdateModal = false;
    this.selectedPostId = undefined;
    this.updateForm = { title: '', content: '' };
    this.removeFile(fileInput);
  }

  ngOnInit() {
    this.postService.getPosts(0).subscribe({
      next: (data) => (this.posts = data),
      error: (err) => console.error(err),
    });
  }
}
