package gr.myprojects.schedulr.dto.authentication;

import gr.myprojects.schedulr.core.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO for authentication response containing token, UUID, and user role")
public class AuthenticationResponseDTO {
    @Schema(description = "JWT token issued to the user", example = "eyJhbGciOiJIUzI1...")
    private String token;

    @Schema(description = "The UUID of the authenticated user", example = "123e4567-e89b-12d3-a456-426614174000")
    private String uuid;

    @Schema(description = "Role assigned to the user", example = "ADMIN")
    private Role role;
}
