import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-report-modal-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './report-modal-component.html',
  styleUrl: './report-modal-component.css'
})
export class ReportModalComponent {
  @Input() show = false;
  @Input() type: 'post' | 'comment' | 'user' = 'post';
  @Input() targetId?: number ;
  @Output() closed = new EventEmitter<void>();
  @Output() submitted = new EventEmitter<{ type: string; targetId?: number; description: string }>();

  description = '';

  close() {
    this.description = '';
    this.closed.emit();
  }

  submit() {
    if (!this.description.trim()) return;
    this.submitted.emit({
      type: this.type,
      targetId: this.targetId,
      description: this.description.trim()
    });
    this.close();
  }
}
