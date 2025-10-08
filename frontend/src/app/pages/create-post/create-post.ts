import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule } from 'lucide-angular';
import { MediaService } from '../../service/media-service';
import { PostService } from '../../service/post-service';
import { QuillModule } from 'ngx-quill';
import Quill from 'quill';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule, QuillModule, NavbarComponent],
  templateUrl: './create-post.html',
  styleUrls: ['./create-post.css'],
})
export class CreatePost {
  private mediaService = inject(MediaService);
  private postService = inject(PostService);
  private router = inject(Router);
  quill!: Quill;
  modules = {
    toolbar: {
      container: [
        ['bold', 'italic'],
        [{ header: 1 }, { header: 2 }, { header: 3 }],
        [{ list: 'ordered' }, { list: 'bullet' }],
        ['blockquote', 'code-block'],
        ['image', 'video'],
      ],
      handlers: {
        image: () => this.handleImageUpload(),
        video: () => this.handleVideoUpload(),
      },
    },
  };

  handleImageUpload() {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'image/*';
    input.onchange = () => {
      if (!input.files?.length) return;
      const file = input.files[0];
      const form = new FormData();
      form.append('file', file);

      this.mediaService.addPostImage(form).subscribe({
        next: (url: string) => {
          const range = this.quill.getSelection(true);
          this.quill.insertEmbed(range.index, 'image', url);
          this.quill.insertText(range.index + 1, '\n\n');
          this.quill.setSelection(range.index + 3, 0);
        },
        error: (err) => console.error(err),
      });
    };
    input.click();
  }

  handleVideoUpload() {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'video/*';
    input.onchange = () => {
      if (!input.files?.length) return;
      const file = input.files[0];
      const form = new FormData();
      form.append('file', file);

      this.mediaService.addPostVideo(form).subscribe({
        next: (url: string) => {
          const range = this.quill.getSelection(true);
          this.quill.clipboard.dangerouslyPasteHTML(
            range.index,
            `<video controls src="${url}" style="max-width:100%"></video>`
          );
          this.quill.insertText(range.index + 1, '\n\n');
          this.quill.setSelection(range.index + 3, 0);
        },
        error: (err) => console.error(err),
      });
    };
    input.click();
  }

  onEditorCreated(quill: Quill) {
    this.quill = quill;
    this.quill.root.style.height = '200px';
    this.quill.on('text-change', () => {
      this.content = this.quill.root.innerHTML;
    });
  }

  title = '';
  content = '';

  onSubmit() {
    console.log(this.content);
    if (!this.title.trim() || !this.content.trim()) return;
    const payload: postRequest = {
      title: this.title,
      content: this.content,
    };

    this.postService.addPost(payload).subscribe({
      next: (res) => {
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error(err);
      },
    });
  }
}
