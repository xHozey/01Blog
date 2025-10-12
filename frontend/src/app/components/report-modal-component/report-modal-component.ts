import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../service/user-service';

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
  @Output() closed = new EventEmitter<void>();

  private userService = inject(UserService);

  description = '';

  close() {
    this.description = '';
    this.closed.emit();
  }

  submit() {
    if (!this.description.trim() || !this.targetId) return;

    const payload: reportRequest = {
      id: this.targetId,
      description: this.description,
    };
    this.userService.reportUser(payload).subscribe({
      next: (res) => {
        console.log('hello world');
        this.close();
      },
      error: (err) => console.error(err),
    });
  }
}
