package gr.myprojects.schedulr.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "DTO for creating a new comment")
public class CommentCreateDTO {

    @NotEmpty(message = "Comment description should not be empty")
    @Schema(description = "The description of the comment", example = "This is a great event!")
    private String description;

    @NotEmpty(message = "User UUID should not be empty")
    @Schema(description = "The UUID of the user creating the comment", example = "123e4567-e89b-12d3-a456-426614174000")
    private String userUuid;

    @NotEmpty(message = "Event UUID should not be empty")
    @Schema(description = "The UUID of the event for which the comment is being created", example = "456e7890-a12b-34c5-d678-901f12345678")
    private String eventUuid;
}
