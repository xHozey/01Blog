import { Component, Input } from '@angular/core';
import { commentResponse } from '../../models/commentResponse';

@Component({
  selector: 'app-comment-component',
  imports: [],
  templateUrl: './comment-component.html',
  styleUrl: './comment-component.css'
})
export class CommentComponent {
  @Input() comment!: commentResponse;

}
