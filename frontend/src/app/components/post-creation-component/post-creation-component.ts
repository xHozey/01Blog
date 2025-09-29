import { Component, ElementRef, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-post-creation-component',
  imports: [CommonModule],
  templateUrl: './post-creation-component.html',
  styleUrl: './post-creation-component.css',
})
export class PostCreationComponent {
  expandForm = false;

  constructor(private elRef: ElementRef) {}

  @HostListener('document:click', ['$event'])
  onClickOutside(event: Event) {
    if (!this.elRef.nativeElement.contains(event.target)) {
      this.expandForm = false;
    }
  }
}
