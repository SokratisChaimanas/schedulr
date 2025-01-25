package gr.myprojects.schedulr.rest;

import gr.myprojects.schedulr.authentication.AuthenticationService;
import gr.myprojects.schedulr.core.exceptions.*;
import gr.myprojects.schedulr.dto.authentication.AuthenticationRequestDTO;
import gr.myprojects.schedulr.dto.authentication.AuthenticationResponseDTO;
import gr.myprojects.schedulr.dto.response.SuccessResponseDTO;
import gr.myprojects.schedulr.dto.user.UserReadOnlyDTO;
import gr.myprojects.schedulr.dto.user.UserRegisterDTO;
import gr.myprojects.schedulr.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user", description = "Registers a new user and returns user details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDTO<UserReadOnlyDTO>> registerUser(
            @Valid @RequestBody UserRegisterDTO userRegisterDTO,
            BindingResult bindingResult
    ) throws ValidationException, AppServerException, AppObjectAlreadyExistsException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("User", bindingResult);
        }

        UserReadOnlyDTO readOnlyDTO = userService.registerUser(userRegisterDTO);

        SuccessResponseDTO<UserReadOnlyDTO> responseDTO = SuccessResponseDTO.<UserReadOnlyDTO>builder()
                .status(HttpStatus.CREATED)
                .data(readOnlyDTO)
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Authenticate a user", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully authenticated"),
            @ApiResponse(responseCode = "403", description = "User not authorized")
    })
    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDTO<AuthenticationResponseDTO>> loginUser(
            @RequestBody AuthenticationRequestDTO authenticationRequestDTO) throws UserNotAuthorizedException {
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.authenticate(authenticationRequestDTO);

        SuccessResponseDTO<AuthenticationResponseDTO> successResponseDTO = SuccessResponseDTO.<AuthenticationResponseDTO>builder()
                .status(HttpStatus.OK)
                .data(authenticationResponseDTO)
                .build();

        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }
}
