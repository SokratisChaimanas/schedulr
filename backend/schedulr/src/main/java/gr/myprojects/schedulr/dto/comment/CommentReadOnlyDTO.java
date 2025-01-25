package gr.myprojects.schedulr.dto.comment;

import gr.myprojects.schedulr.dto.user.UserReadOnlyDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "DTO for reading a comment")
public class CommentReadOnlyDTO {
    @Schema(description = "The UUID of the comment", example = "789e4567-d12c-34a5-b678-890f12345678")
    private String uuid;

    @Schema(description = "The description of the comment", example = "This is a great event!")
    private String description;

    @Schema(description = "The username of the author", example = "john_doe")
    private String authorUsername;

    @Schema(description = "The title of the event", example = "Spring Boot Workshop")
    private String eventTitle;

    @Schema(description = "The UUID of the event", example = "456e7890-a12b-34c5-d678-901f12345678")
    private String eventUuid;

    @Schema(description = "Indicates whether the comment is deleted", example = "false")
    private Boolean isDeleted;
}
