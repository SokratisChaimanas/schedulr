package gr.myprojects.schedulr.dto.user;

import gr.myprojects.schedulr.core.enums.Role;
import gr.myprojects.schedulr.validation.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@PasswordMatches
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "DTO for user registration")
public class UserRegisterDTO {
    @NotEmpty(message = "Username must not be empty")
    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Email(message = "Invalid email")
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @Pattern(
            regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[!@#$%^&*]).{8,}$",
            message = "Invalid Password Format"
    )
    @Schema(description = "Password of the user", example = "P@ssw0rd!")
    private String password;

    @NotEmpty(message = "Firstname must not be empty")
    @Schema(description = "First name of the user", example = "John")
    private String firstname;

    @NotEmpty(message = "Lastname must not be empty")
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastname;

    @NotNull(message = "Role must not be empty")
    @Schema(description = "Role of the user", example = "USER")
    private String role;

    @NotBlank(message = "Confirm Password should not be empty")
    @Schema(description = "The confirmation password field, must be the same as password")
    private String confirmPassword;
}
