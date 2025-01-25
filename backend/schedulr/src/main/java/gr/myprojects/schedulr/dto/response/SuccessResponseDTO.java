package gr.myprojects.schedulr.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "DTO for representing success responses")
public class SuccessResponseDTO<T> {
    @Schema(description = "HTTP status of the success response", example = "200")
    private HttpStatus status;

    @Schema(description = "Data contained in the response")
    private T data;
}
