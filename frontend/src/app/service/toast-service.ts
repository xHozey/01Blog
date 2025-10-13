import { Injectable } from '@angular/core';

export interface Toast {
  id: string;
  message: string;
  type: 'success' | 'error' | 'warning';
}

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  toasts: Toast[] = [];

  show(type: Toast['type'], message: string) {
    const toast: Toast = { id: Date.now().toString(), type, message };
    this.toasts.push(toast);

    setTimeout(() => {
      this.toasts = this.toasts.filter((t) => t !== toast);
    }, 3000);
  }

  success(message: string) {
    this.show('success', message);
  }

  error(message: string) {
    this.show('error', message);
  }

  warning(message: string) {
    this.show('warning', message);
  }
}
