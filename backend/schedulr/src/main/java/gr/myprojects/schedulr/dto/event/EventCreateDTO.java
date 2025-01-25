package gr.myprojects.schedulr.dto.event;

import gr.myprojects.schedulr.core.enums.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "DTO for creating a new event")
public class EventCreateDTO {
    @NotEmpty
    @Size(max = 255)
    @Schema(description = "Title of the event", example = "Spring Boot Workshop")
    private String title;

    @NotEmpty
    @Size(max = 1000)
    @Schema(description = "Description of the event", example = "Learn Spring Boot in a day!")
    private String description;

    @NotNull
    @Schema(description = "Date and time of the event", example = "2025-02-01T10:00:00")
    private LocalDateTime date;

    @NotEmpty
    @Schema(description = "Location of the event", example = "New York City")
    private String location;

    @NotNull
    @Min(0)
    @Schema(description = "Price of the event", example = "49.99")
    private Double price;

    @NotNull
    @Min(0)
    @Schema(description = "Maximum number of attendees allowed", example = "100")
    private Integer maxSeats;

    @NotNull
    @Schema(description = "Category of the event", example = "WORKSHOP")
    private Category category;
}
