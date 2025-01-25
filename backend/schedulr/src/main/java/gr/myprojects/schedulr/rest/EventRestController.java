package gr.myprojects.schedulr.rest;

import gr.myprojects.schedulr.core.enums.Status;
import gr.myprojects.schedulr.core.exceptions.*;
import gr.myprojects.schedulr.core.filters.Paginated;
import gr.myprojects.schedulr.dto.event.*;
import gr.myprojects.schedulr.dto.response.SuccessResponseDTO;
import gr.myprojects.schedulr.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventRestController {
    private final EventService eventService;

    @Operation(summary = "Create a new event", description = "Creates a new event with optional image attachment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ValidationException.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDTO<EventReadOnlyDTO>> createEvent(
            @Valid @RequestPart("createDTO") EventCreateDTO createDTO,
            @RequestPart("uuid") String uuid,
            @RequestPart(name = "imageFile", required = false) MultipartFile imageFile,
            BindingResult bindingResult)
            throws ValidationException, AppObjectInvalidArgumentException, AppObjectNotFoundException, IOException, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("ImageAttachment", bindingResult);
        }

        EventReadOnlyDTO readOnlyDTO = eventService.saveEvent(createDTO, uuid, imageFile);
        SuccessResponseDTO<EventReadOnlyDTO> successResponseDTO = SuccessResponseDTO.<EventReadOnlyDTO>builder()
                .status(HttpStatus.CREATED)
                .data(readOnlyDTO)
                .build();

        return new ResponseEntity<>(successResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Cancel an event", description = "Cancels an event by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event successfully canceled"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/cancel")
    public ResponseEntity<SuccessResponseDTO<EventReadOnlyDTO>> cancelEvent(
            @RequestBody EventCancelDTO cancelDTO
    ) throws AppObjectNotFoundException, AccessDeniedException, AppServerException {

        EventReadOnlyDTO eventReadOnlyDTO = eventService.cancelEvent(cancelDTO.getEventUuid(), cancelDTO.getUserUuid());
        SuccessResponseDTO<EventReadOnlyDTO> successResponseDTO = SuccessResponseDTO.<EventReadOnlyDTO>builder()
                .status(HttpStatus.OK)
                .data(eventReadOnlyDTO)
                .build();

        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Attend an event", description = "Allows a user to attend an event by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully attended the event"),
            @ApiResponse(responseCode = "404", description = "Event or user not found"),
            @ApiResponse(responseCode = "400", description = "Event is full or unavailable")
    })
    @PostMapping("/attend")
    public ResponseEntity<SuccessResponseDTO<EventReadOnlyDTO>> attendEvent(
            @RequestBody EventAttendDTO eventAttendDTO
    ) throws AppObjectNotFoundException, AccessDeniedException, EventFullException, AppObjectUnavailableException, AppObjectInvalidArgumentException, AppServerException {

        EventReadOnlyDTO readOnlyDTO = eventService.attendEvent(eventAttendDTO);
        SuccessResponseDTO<EventReadOnlyDTO> successResponseDTO = SuccessResponseDTO.<EventReadOnlyDTO>builder()
                .status(HttpStatus.OK)
                .data(readOnlyDTO)
                .build();

        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get paginated events", description = "Retrieve a paginated list of events filtered by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved events")
    })
    @GetMapping("")
    public ResponseEntity<SuccessResponseDTO<Page<EventReadOnlyDTO>>> getPaginatedEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "PENDING") Status status
    ) {
        Page<EventReadOnlyDTO> events = eventService.getPaginatedEventsByStatus(status, page, size);
        SuccessResponseDTO<Page<EventReadOnlyDTO>> successResponseDTO = SuccessResponseDTO.<Page<EventReadOnlyDTO>>builder()
                .status(HttpStatus.OK)
                .data(events)
                .build();

        return new ResponseEntity<>(successResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get paginated filtered events", description = "Retrieve a paginated list of events based on filter criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered events")
    })
    @PostMapping("/filters/paginated")
    public ResponseEntity<SuccessResponseDTO<Paginated<EventReadOnlyDTO>>> getPaginatedFilteredEvents(
            @RequestBody EventFilterDTO filterDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Paginated<EventReadOnlyDTO> events = eventService.getPaginatedFilteredEvents(filterDTO, page, size);
        SuccessResponseDTO<Paginated<EventReadOnlyDTO>> responseDTO = SuccessResponseDTO.<Paginated<EventReadOnlyDTO>>builder()
                .status(HttpStatus.OK)
                .data(events)
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get filtered events", description = "Retrieve a list of events based on filter criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered events")
    })
    @PostMapping("/filters")
    public ResponseEntity<SuccessResponseDTO<List<EventReadOnlyDTO>>> getFilteredEvents(
            @RequestBody @Valid EventFilterDTO filterDTO
    ) {
        List<EventReadOnlyDTO> events = eventService.getFilteredEvents(filterDTO);
        SuccessResponseDTO<List<EventReadOnlyDTO>> responseDTO = SuccessResponseDTO.<List<EventReadOnlyDTO>>builder()
                .status(HttpStatus.OK)
                .data(events)
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
