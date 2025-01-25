package gr.myprojects.schedulr.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "DTO for deleting a comment")
public class CommentDeleteDTO {
    @Schema(description = "The UUID of the comment to be deleted", example = "789e4567-d12c-34a5-b678-890f12345678")
    private String commentUuid;

    @Schema(description = "The UUID of the user deleting the comment", example = "123e4567-e89b-12d3-a456-426614174000")
    private String userUuid;
}
