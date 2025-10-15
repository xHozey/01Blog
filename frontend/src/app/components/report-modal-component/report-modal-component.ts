import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../service/user-service';
import { ToastService } from '../../service/toast-service';
import { parseApiError } from '../../utils/errorHelper';
import { PostService } from '../../service/post-service';

@Component({
  selector: 'app-report-modal-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './report-modal-component.html',
  styleUrl: './report-modal-component.css',
})
export class ReportModalComponent {
  @Input() show = false;
  @Input() targetId?: number;
  @Input() type: 'post' | 'user' = 'user';
  @Input() postId?: number;
  @Output() closed = new EventEmitter<void>();

  private userService = inject(UserService);
  private toastService = inject(ToastService);
  private postService = inject(PostService);

  confirming = false;
  description = '';
  theme = localStorage.getItem('theme');

  close() {
    this.description = '';
    this.closed.emit();
  }

  submit() {
    if (!this.description.trim() || !this.targetId) return;
    this.show = false;
    this.confirming = true;
  }

  confirmReport() {
    const payload: reportRequest = {
      id: this.targetId!,
      description: this.description,
    };

    switch (this.type) {
      case 'user':
        this.userService.reportUser(payload).subscribe({
          next: (msg) => {
            this.toastService.success(msg);
            this.closeConfirmation();
            this.close();
          },
          error: (err) => parseApiError(err).forEach((msg) => this.toastService.error(msg)),
        });
        break;
      case 'post':
        if (!this.postId) return;
        this.postService.reportPost(payload, this.postId).subscribe({
          next: (msg) => {
            this.toastService.success(msg);
            this.closeConfirmation();
            this.close();
          },
          error: (err) => parseApiError(err).forEach((msg) => this.toastService.error(msg)),
        });
        break;
    }
  }

  closeConfirmation() {
    this.confirming = false;
    this.show = true;
  }
}
