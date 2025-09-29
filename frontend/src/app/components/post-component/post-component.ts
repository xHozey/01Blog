import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-post-component',
  imports: [CommonModule],
  templateUrl: './post-component.html',
  styleUrl: './post-component.css',
})
export class PostComponent {
  @Input() post!: postResponse;
  showComments = false;

  toggleComments() {
    this.showComments = !this.showComments;
  }
}
