import {Component, EventEmitter, inject, Input, Output, signal} from '@angular/core';
import {DatePipe} from "@angular/common";
import {AuthService} from "../../../../shared/services/auth.service";
import {CommentService} from "../../../../shared/services/comment.service";
import {CommentReadOnly, CommentDelete} from "../../../../shared/interfaces/Comment";

@Component({
  selector: 'app-comment',
  standalone: true,
  imports: [
    DatePipe
  ],
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent {
  @Input({ required: true }) comment!: CommentReadOnly;
  @Output() commentDeleted = new EventEmitter<string>();
  
  authService = inject(AuthService);
  commentService = inject(CommentService);
  isEnabled = signal<boolean>(false);
  
  ngOnChanges() {
    if (this.comment) {
      const loggedInUserUsername = this.authService.loggedInUserUsername;
      const loggedInUserRole = this.authService.loggedInUserRole;
      
      this.isEnabled.set(
          loggedInUserUsername() === this.comment.authorUsername || loggedInUserRole() === 'ADMIN'
      );
    }
  }
  
  deleteComment() {
    const commentDelete: CommentDelete = {
      commentUuid: this.comment.uuid,
      userUuid: this.authService.loggedInUserUuid()!
    };
    
    this.commentService.deleteComment(commentDelete).subscribe({
      next: () => {
        console.log("Comment deleted successfully.");
        this.commentDeleted.emit(this.comment.uuid); // Notify parent component about deletion
      },
      error: (err) => {
        console.error("Failed to delete comment:", err);
      }
    });
  }
}
