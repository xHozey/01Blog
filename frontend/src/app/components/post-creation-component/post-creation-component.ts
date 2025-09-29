import { CommonModule } from '@angular/common';
import { Component, ElementRef, HostListener } from '@angular/core';
@Component({
  selector: 'app-post-creation-component',
  imports: [CommonModule],
  templateUrl: './post-creation-component.html',
  styleUrl: './post-creation-component.css',
})
export class PostCreationComponent {
  expandForm = false;
  title = '';
  content = '';
  previewUrl: string | null = null;
  selectedFile: File | null = null;

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

    const newPost: postRequest = {
      title: this.title,
      content: this.content,
      imagePath: this.selectedFile?.type.startsWith('image/') ? this.previewUrl || '' : '',
      videoPath: this.selectedFile?.type.startsWith('video/') ? this.previewUrl || '' : '',
    };

    console.log('Submitting post:', newPost);
    // TODO: send to API or add to local posts array

    // Reset form
    this.handleCancel();
  }

  handleCancel() {
    this.title = '';
    this.content = '';
    this.removeFile();
    this.expandForm = false;
  }
}
