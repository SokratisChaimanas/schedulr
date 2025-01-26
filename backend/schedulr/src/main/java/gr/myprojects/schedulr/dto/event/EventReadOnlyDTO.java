package gr.myprojects.schedulr.dto.event;

import gr.myprojects.schedulr.core.enums.Category;
import gr.myprojects.schedulr.core.enums.Status;
import gr.myprojects.schedulr.dto.comment.CommentReadOnlyDTO;
import gr.myprojects.schedulr.dto.user.UserReadOnlyDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "Read-only DTO representing an event")
public class EventReadOnlyDTO {
    @Schema(description = "Status of the event", example = "PENDING")
    private Status status;

    @Schema(description = "UUID of the event", example = "123e4567-e89b-12d3-a456-426614174000")
    private String uuid;

    @Schema(description = "Title of the event", example = "Spring Boot Workshop")
    private String title;

    @Schema(description = "Description of the event", example = "Learn Spring Boot in a day!")
    private String description;

    @Schema(description = "The category of the event")
    private Category category;

    @Schema(description = "Date and time of the event", example = "2025-02-01T10:00:00")
    private LocalDateTime date;

    @Schema(description = "Details of the image attachment", implementation = ImageAttachmentReadOnlyDTO.class)
    private ImageAttachmentReadOnlyDTO imageAttachmentReadOnlyDTO;

    @Schema(description = "Owner of the event", implementation = UserReadOnlyDTO.class)
    private UserReadOnlyDTO ownerReadOnlyDTO;

    @Schema(description = "Price of the event", example = "49.99")
    private Double price;

    @Schema(description = "List of comments for the event", implementation = CommentReadOnlyDTO.class)
    private List<CommentReadOnlyDTO> commentsList;

    @Schema(description = "Maximun capacity of the event")
    private Integer maxSeats;

    @Schema(description = "The booked seats")
    private Integer bookedSeats;
}
