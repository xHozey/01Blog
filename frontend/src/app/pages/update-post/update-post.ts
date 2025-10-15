import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule } from 'lucide-angular';
import { MediaService } from '../../service/media-service';
import { PostService } from '../../service/post-service';
import { QuillModule } from 'ngx-quill';
import Quill from 'quill';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastService } from '../../service/toast-service';
import { parseApiError } from '../../utils/errorHelper';

@Component({
  selector: 'app-update-post',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule, QuillModule, NavbarComponent],
  templateUrl: './update-post.html',
  styleUrls: ['./update-post.css'],
})
export class UpdatePost implements OnInit {
  private route = inject(ActivatedRoute);
  private mediaService = inject(MediaService);
  private postService = inject(PostService);
  private router = inject(Router);
  private toastService = inject(ToastService);
  postId!: number;
  post!: postResponse;
  title = '';
  content = '';

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

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.postId = idParam ? +idParam : 0;

    this.postService.getPost(this.postId).subscribe({
      next: (res) => {
        this.post = res;
        this.title = this.post.title;
        if (this.quill) {
          this.quill.root.innerHTML = this.post.content;
          this.content = this.post.content;
        }
      },
      error: (err) => parseApiError(err).forEach((msg) => this.toastService.error(msg)),
    });
  }

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
        error: (err) => parseApiError(err).forEach((msg) => this.toastService.error(msg)),
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
        error: (err) => parseApiError(err).forEach((msg) => this.toastService.error(msg)),
      });
    };
    input.click();
  }

  onEditorCreated(quill: Quill) {
    this.quill = quill;
    this.quill.root.style.minHeight = '200px';

    if (this.post) {
      this.quill.setContents(this.quill.clipboard.convert({ html: this.post.content }));
      this.content = this.post.content;
    }

    this.quill.on('text-change', () => {
      this.content = this.quill.root.innerHTML;
    });

    console.log(this.post.content);
  }

  onSubmit() {
    console.log(this.content);
    if (!this.title.trim() || !this.content.trim()) return;
    const payload: postRequest = {
      title: this.title.trim(),
      content: this.content.trim(),
    };

    this.postService.updatePost(payload, this.post.id).subscribe({
      next: (res) => {
        console.log(res);
        this.router.navigate(['/']);
      },
      error: (err) => {
        parseApiError(err).forEach((msg) => this.toastService.error(msg));
      },
    });
  }
}
