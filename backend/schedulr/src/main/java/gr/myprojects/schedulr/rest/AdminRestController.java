package gr.myprojects.schedulr.rest;

import gr.myprojects.schedulr.core.exceptions.AppObjectNotFoundException;
import gr.myprojects.schedulr.core.exceptions.AppServerException;
import gr.myprojects.schedulr.dto.event.EventReadOnlyDTO;
import gr.myprojects.schedulr.dto.response.SuccessResponseDTO;
import gr.myprojects.schedulr.service.EventService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin API", description = "API for admin-related operations")
public class AdminRestController {
    private final EventService eventService;

    @Operation(summary = "Get all events by status", description = "Retrieve all events filtered by their status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved events"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @GetMapping("/events/status")
    public ResponseEntity<SuccessResponseDTO<List<EventReadOnlyDTO>>> getAllEventsByStatus(
            @RequestParam String userUuid,
            @RequestParam String status
    ) throws AppObjectNotFoundException, AppServerException {
        List<EventReadOnlyDTO> events = eventService.getAllEventsByStatus(userUuid, status);

        SuccessResponseDTO<List<EventReadOnlyDTO>> responseDTO = SuccessResponseDTO.<List<EventReadOnlyDTO>>builder()
                .status(HttpStatus.OK)
                .data(events)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
