import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
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
import { PostService } from '../../service/post-service';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { Editor, NgxEditorComponent, NgxEditorMenuComponent } from 'ngx-editor';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [NgxEditorComponent, NgxEditorMenuComponent,CommonModule, FormsModule, LucideAngularModule, NavbarComponent],
  templateUrl: './create-post.html',
  styleUrls: ['./create-post.css'],
})
export class CreatePost implements OnInit, OnDestroy {
  readonly ArrowLeftIcon = ArrowLeft;
  readonly ImageIcon = ImageIcon;
  readonly SendIcon = SendHorizontal;
  readonly UploadIcon = Upload;
  readonly XIcon = X;
  editor: Editor | null = null;

  title = '';
  content = '';
  previewUrl: string | null = null;
  selectedFile: File | null = null;
  dragOver = false;

  @ViewChild('fileInputRef') fileInputRef!: ElementRef<HTMLInputElement>;

  constructor(private postService: PostService, private router: Router) {}

  // --- File handling ---
  onFileSelect(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) this.setFile(input.files[0]);
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
    const file = event.dataTransfer?.files?.[0];
    if (file) this.setFile(file);
  }

  private setFile(file: File) {
    if (!file.type.startsWith('image/') && !file.type.startsWith('video/')) return;
    this.selectedFile = file;

    const reader = new FileReader();
    reader.onload = () => (this.previewUrl = reader.result as string);
    reader.readAsDataURL(file);
  }

  get isImage() {
    return this.selectedFile?.type.startsWith('image/');
  }

  get isVideo() {
    return this.selectedFile?.type.startsWith('video/');
  }

  removeFile() {
    this.selectedFile = null;
    this.previewUrl = null;
    if (this.fileInputRef?.nativeElement) {
      this.fileInputRef.nativeElement.value = '';
    }
  }

  // --- Submit ---
  handleSubmit() {
    if (!this.content.trim()) return;

    const formData = new FormData();
    formData.append('title', this.title);
    formData.append('content', this.content);
    if (this.selectedFile) formData.append('file', this.selectedFile);

    this.postService.addPost(formData).subscribe({
      next: () => {
        this.resetForm();
        this.router.navigate(['/']);
      },
      error: (err) => console.error(err),
    });
  }

  handleCancel() {
    if (this.title || this.content || this.selectedFile) {
      if (confirm('Discard changes?')) this.resetFormAndExit();
    } else {
      this.router.navigate(['/']);
    }
  }

  private resetForm() {
    this.title = '';
    this.content = '';
    this.selectedFile = null;
    this.previewUrl = null;
    if (this.fileInputRef?.nativeElement) {
      this.fileInputRef.nativeElement.value = '';
    }
  }

  private resetFormAndExit() {
    this.resetForm();
    this.router.navigate(['/']);
  }

  ngOnInit(): void {
      this.editor = new Editor()
  }

  ngOnDestroy(): void {
      this.editor?.destroy()
  }
}
