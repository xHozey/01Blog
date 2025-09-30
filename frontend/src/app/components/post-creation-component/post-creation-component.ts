import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Image, LucideAngularModule, SendHorizontal, Video } from 'lucide-angular';
import { PostService } from '../../service/post-service';
@Component({
  selector: 'app-post-creation-component',
  imports: [CommonModule, FormsModule, LucideAngularModule],
  templateUrl: './post-creation-component.html',
  styleUrl: './post-creation-component.css',
})
export class PostCreationComponent {
  @Output() postCreated = new EventEmitter<postResponse>();
  readonly ImageIcon = Image;
  readonly VideoIcon = Video;
  readonly SendIcon = SendHorizontal;
  expandForm = false;
  title = '';
  content = '';
  previewUrl: string | null = null;
  selectedFile: File | null = null;

  constructor(private postService: PostService) {}
  // ----------------------
  // File selection
  // ----------------------
  onFileSelect(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;

    const file = input.files[0];
    this.selectedFile = file;

    const reader = new FileReader();
    reader.onload = () => {
      this.previewUrl = reader.result as string;
    };
    reader.readAsDataURL(file);
  }

  removeFile() {
    this.selectedFile = null;
    this.previewUrl = null;
  }

  // ----------------------
  // Submit & Cancel
  // ----------------------
  handleSubmit() {
    if (!this.content.trim()) return;

    const payload: postRequest = {
      title: this.title,
      content: this.content,
      imagePath: this.selectedFile?.type.startsWith('image/') ? this.previewUrl || '' : '',
      videoPath: this.selectedFile?.type.startsWith('video/') ? this.previewUrl || '' : '',
    };

    console.log('Submitting post:', payload);
    this.postService.addPost(payload).subscribe({
      next: (data) => {
        this.postCreated.emit(data)
      },
      error: (err) => {
        console.error(err);
      },
    });
    this.handleCancel();
  }

  handleCancel() {
    this.title = '';
    this.content = '';
    this.removeFile();
    this.expandForm = false;
  }
}
