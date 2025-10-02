import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {
  ArrowLeft,
  Image as ImageIcon,
  SendHorizontal,
  Upload,
  X,
  LucideAngularModule,
} from 'lucide-angular';
import { PostService } from '../../service/post-service'; // Adjust path as needed
import { NavbarComponent } from '../../components/navbar-component/navbar-component';

interface postResponse {
  // Define as needed
}

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule, NavbarComponent],
  templateUrl: './create-post.html',
  styleUrls: ['./create-post.css'],
})
export class CreatePost {
  readonly ArrowLeftIcon = ArrowLeft;
  readonly ImageIcon = ImageIcon;
  readonly SendIcon = SendHorizontal;
  readonly UploadIcon = Upload;
  readonly XIcon = X;

  title = '';
  content = '';
  previewUrl: string | null = null;
  selectedFile: File | null = null;
  dragOver = false;

  @ViewChild('fileInputRef') fileInputRef!: ElementRef<HTMLInputElement>;

  constructor(private postService: PostService, private router: Router) {}

  onFileSelect(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.handleFile(input.files[0]);
    }
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
    if (files?.length) {
      this.handleFile(files[0]);
    }
  }

  private handleFile(file: File) {
    if (file.type.startsWith('image/') || file.type.startsWith('video/')) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  get isFileImage(): boolean {
    return !!this.selectedFile?.type?.startsWith('image/');
  }

  get isFileVideo(): boolean {
    return !!this.selectedFile?.type?.startsWith('video/');
  }

  removeFile() {
    this.selectedFile = null;
    this.previewUrl = null;
    this.fileInputRef.nativeElement.value = '';
  }

  handleSubmit() {
    if (!this.content.trim()) return;

    const formData = new FormData();
    formData.append('title', this.title);
    formData.append('content', this.content);
    if (this.selectedFile) {
      formData.append(
        this.selectedFile.type.startsWith('image/') ? 'image' : 'video',
        this.selectedFile
      );
    }
    
    this.postService.addPost(formData).subscribe({
      next: (data: postResponse) => {
        this.resetForm();
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  handleCancel() {
    if (this.title || this.content || this.selectedFile) {
      if (confirm('Discard changes?')) {
        this.resetForm();
        this.router.navigate(['/']);
      }
    } else {
      this.router.navigate(['/']);
    }
  }

  private resetForm() {
    this.title = '';
    this.content = '';
    this.selectedFile = null;
    this.previewUrl = null;
    this.fileInputRef.nativeElement.value = '';
  }
}
