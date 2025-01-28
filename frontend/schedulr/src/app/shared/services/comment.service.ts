import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs';
import { SuccessResponse } from '../interfaces/Response';
import { CommentCreate, CommentDelete, CommentReadOnly } from '../interfaces/Comment';

@Injectable({
    providedIn: 'root',
})
export class CommentService {
    private http = inject(HttpClient);
    private API_URL_COMMENTS = 'http://localhost:8080/api/comments';
    
    /**
     * Create a new comment for a specific event
     * @param commentCreate - Data to create the comment
     * @returns Observable of the created comment
     */
    createComment(commentCreate: CommentCreate) {
        return this.http
            .post<SuccessResponse<CommentReadOnly>>(`${this.API_URL_COMMENTS}/create`, commentCreate)
            .pipe(map((response) => response.data));
    }
    
    /**
     * Delete a comment by its UUID
     * @param commentDelete - Data to identify the comment and user
     * @returns Observable of the deleted comment
     */
    deleteComment(commentDelete: CommentDelete) {
        return this.http
            .put<SuccessResponse<CommentReadOnly>>(`${this.API_URL_COMMENTS}/delete`, commentDelete)
            .pipe(map((response) => response.data));
    }
}
