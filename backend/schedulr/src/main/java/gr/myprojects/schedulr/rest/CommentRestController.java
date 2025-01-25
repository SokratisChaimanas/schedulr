package gr.myprojects.schedulr.rest;

import gr.myprojects.schedulr.core.exceptions.*;
import gr.myprojects.schedulr.dto.comment.CommentCreateDTO;
import gr.myprojects.schedulr.dto.comment.CommentDeleteDTO;
import gr.myprojects.schedulr.dto.comment.CommentReadOnlyDTO;
import gr.myprojects.schedulr.dto.response.SuccessResponseDTO;
import gr.myprojects.schedulr.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;

    @Operation(summary = "Create a new comment", description = "Creates a comment for a specific event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment successfully created"),
            @ApiResponse(responseCode = "404", description = "Event or User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDTO<CommentReadOnlyDTO>> createComment(
            @RequestBody @Valid CommentCreateDTO commentCreateDTO
    ) throws AppObjectNotFoundException, AppObjectInvalidArgumentException, AppServerException {
        String userUuid = commentCreateDTO.getUserUuid();
        String eventUuid = commentCreateDTO.getEventUuid();

        CommentReadOnlyDTO createdComment = commentService.createComment(commentCreateDTO, userUuid, eventUuid);

        SuccessResponseDTO<CommentReadOnlyDTO> successResponseDTO = SuccessResponseDTO.<CommentReadOnlyDTO>builder()
                .status(HttpStatus.CREATED)
                .data(createdComment)
                .build();

        return new ResponseEntity<>(successResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a comment", description = "Deletes a comment by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Comment or User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/delete")
    public ResponseEntity<SuccessResponseDTO<CommentReadOnlyDTO>> deleteComment(
            @RequestBody CommentDeleteDTO commentDeleteDTO
    ) throws AppObjectNotFoundException, AccessDeniedException, AppServerException {
        String userUuid = commentDeleteDTO.getUserUuid();
        String commentUuid = commentDeleteDTO.getCommentUuid();

        CommentReadOnlyDTO deletedComment = commentService.deleteComment(userUuid, commentUuid);

        SuccessResponseDTO<CommentReadOnlyDTO> successResponseDTO = SuccessResponseDTO.<CommentReadOnlyDTO>builder()
                .status(HttpStatus.OK)
                .data(deletedComment)
                .build();

        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }
}
