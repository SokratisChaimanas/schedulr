package gr.myprojects.schedulr.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO for attending an event")
public class EventAttendDTO {
    @Schema(description = "UUID of the event to attend", example = "123e4567-e89b-12d3-a456-426614174000")
    private String eventUuid;

    @Schema(description = "UUID of the user attending the event", example = "456e7890-e12b-34d5-a678-526715174111")
    private String userUuid;
}
