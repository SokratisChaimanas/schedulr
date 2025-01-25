package gr.myprojects.schedulr.dto.event;

import gr.myprojects.schedulr.core.enums.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for filtering events")
public class EventFilterDTO {
    @Schema(description = "Start date for filtering events", example = "2025-01-01")
    private LocalDate startDate;

    @Schema(description = "End date for filtering events", example = "2025-01-31")
    private LocalDate endDate;

    @Schema(description = "Location for filtering events", example = "San Francisco")
    private String location;

    @Schema(description = "Minimum price for filtering events", example = "20.00")
    private Double minPrice;

    @Schema(description = "Maximum price for filtering events", example = "100.00")
    private Double maxPrice;

    @Schema(description = "Category of the event (e.g., WORKSHOP, CONFERENCE)", example = "CONFERENCE")
    private Category category;

    @Schema(description = "Title of the event (supports partial match)", example = "Tech Expo")
    private String title;
}
