package gr.myprojects.schedulr.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "Read-only DTO representing a user")
public class UserReadOnlyDTO {
    @Schema(description = "UUID of the user", example = "123e4567-e89b-12d3-a456-426614174000")
    private String uuid;

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;
}
