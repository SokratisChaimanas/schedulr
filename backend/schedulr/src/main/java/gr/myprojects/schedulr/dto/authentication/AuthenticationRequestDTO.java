package gr.myprojects.schedulr.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO for authentication request containing username and password")
public class AuthenticationRequestDTO {
    @Schema(description = "The username of the user", example = "johndoe")
    private String username;

    @Schema(description = "The password of the user", example = "password123")
    private String password;
}


