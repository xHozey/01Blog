import { CommonModule } from '@angular/common';
import { Component, inject, Input } from '@angular/core';
import { Toast, ToastService } from '../../service/toast-service';

@Component({
  selector: 'app-toast-component',
  imports: [CommonModule],
  templateUrl: './toast-component.html',
  styleUrl: './toast-component.css',
})
export class ToastComponent {
  toastService = inject(ToastService);

  removeToast(toast: Toast) {
    this.toastService.toasts = this.toastService.toasts.filter((t) => t != toast);
  }
  getToastClasses(type: string) {
    switch (type) {
      case 'success':
        return 'bg-success text-white';
      case 'error':
        return 'bg-danger text-white';
      case 'warning':
        return 'bg-warning text-dark';
      default:
        return 'bg-primary text-white';
    }
  }
}
