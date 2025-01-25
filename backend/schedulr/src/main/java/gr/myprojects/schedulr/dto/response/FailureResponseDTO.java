package gr.myprojects.schedulr.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "DTO for representing failure responses")
public class FailureResponseDTO {
    @Schema(description = "HTTP status of the failure response", example = "400")
    private HttpStatus status;

    @Schema(description = "Code indicating the type of error", example = "ValidationError")
    private String code;

    @Schema(description = "Description of the error", example = "Invalid input data")
    private String message;

    @Schema(description = "Additional information about the error")
    private Object extraInformation;
}
