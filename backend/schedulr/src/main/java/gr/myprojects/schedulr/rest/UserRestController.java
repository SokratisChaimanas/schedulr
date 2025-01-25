package gr.myprojects.schedulr.rest;

import gr.myprojects.schedulr.core.exceptions.AppObjectNotFoundException;
import gr.myprojects.schedulr.core.exceptions.AppServerException;
import gr.myprojects.schedulr.dto.event.EventReadOnlyDTO;
import gr.myprojects.schedulr.dto.response.SuccessResponseDTO;
import gr.myprojects.schedulr.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User API", description = "API for user-related operations")
public class UserRestController {
    private final UserService userService;

    @Operation(summary = "Get owned events of a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of owned events retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userUuid}/owned")
    public ResponseEntity<SuccessResponseDTO<List<EventReadOnlyDTO>>> getOwnedEvents(
            @PathVariable("userUuid") String userUuid) throws AppObjectNotFoundException, AppServerException, AccessDeniedException {
        List<EventReadOnlyDTO> ownedEvents = userService.getOwnedEventsByUserUuid(userUuid);

        SuccessResponseDTO<List<EventReadOnlyDTO>> successResponseDTO = SuccessResponseDTO.<List<EventReadOnlyDTO>>builder()
                .status(HttpStatus.OK)
                .data(ownedEvents)
                .build();

        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get pending attended events of a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of pending attended events retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userUuid}/attended-pending")
    public ResponseEntity<SuccessResponseDTO<List<EventReadOnlyDTO>>> getPendingAttendedEvents(
            @PathVariable("userUuid") String userUuid) throws AppObjectNotFoundException, AppServerException {
        List<EventReadOnlyDTO> ownedEvents = userService.getPendingEventsByUserUuid(userUuid);

        SuccessResponseDTO<List<EventReadOnlyDTO>> successResponseDTO = SuccessResponseDTO.<List<EventReadOnlyDTO>>builder()
                .status(HttpStatus.OK)
                .data(ownedEvents)
                .build();

        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get completed attended events of a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of completed attended events retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userUuid}/attended-completed")
    public ResponseEntity<SuccessResponseDTO<List<EventReadOnlyDTO>>> getCompletedAttendedEvents(
            @PathVariable("userUuid") String userUuid) throws AppObjectNotFoundException, AppServerException {
        List<EventReadOnlyDTO> ownedEvents = userService.getCompletedEventsByUserUuid(userUuid);

        SuccessResponseDTO<List<EventReadOnlyDTO>> successResponseDTO = SuccessResponseDTO.<List<EventReadOnlyDTO>>builder()
                .status(HttpStatus.OK)
                .data(ownedEvents)
                .build();

        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get canceled attended events of a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of canceled attended events retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userUuid}/attended-canceled")
    public ResponseEntity<SuccessResponseDTO<List<EventReadOnlyDTO>>> getCanceledAttendedEvents(
            @PathVariable("userUuid") String userUuid) throws AppObjectNotFoundException, AppServerException {
        List<EventReadOnlyDTO> ownedEvents = userService.getCanceledEventsByUserUuid(userUuid);

        SuccessResponseDTO<List<EventReadOnlyDTO>> successResponseDTO = SuccessResponseDTO.<List<EventReadOnlyDTO>>builder()
                .status(HttpStatus.OK)
                .data(ownedEvents)
                .build();

        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }
}
