import {Component, Input} from '@angular/core';
import {EventReadOnly} from "../../../../shared/interfaces/Event";
import {CommentReadOnly} from "../../../../shared/interfaces/Comment";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-comment',
  standalone: true,
  imports: [
    DatePipe
  ],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent {
  @Input({ required: true }) comment!: CommentReadOnly;

}
