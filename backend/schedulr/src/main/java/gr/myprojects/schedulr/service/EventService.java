package gr.myprojects.schedulr.service;

import gr.myprojects.schedulr.core.exceptions.*;
import gr.myprojects.schedulr.core.util.ServiceUtil;
import gr.myprojects.schedulr.service.UserService;
import gr.myprojects.schedulr.core.enums.Role;
import gr.myprojects.schedulr.core.enums.Status;
import gr.myprojects.schedulr.core.filters.EventFilters;
import gr.myprojects.schedulr.core.filters.Paginated;
import gr.myprojects.schedulr.core.mapper.Mapper;
import gr.myprojects.schedulr.dto.event.EventAttendDTO;
import gr.myprojects.schedulr.dto.event.EventCreateDTO;
import gr.myprojects.schedulr.dto.event.EventFilterDTO;
import gr.myprojects.schedulr.dto.event.EventReadOnlyDTO;
import gr.myprojects.schedulr.model.Event;
import gr.myprojects.schedulr.model.ImageAttachment;
import gr.myprojects.schedulr.model.User;
import gr.myprojects.schedulr.repository.EventRepository;
import gr.myprojects.schedulr.repository.UserRepository;
import gr.myprojects.schedulr.specifications.EventSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final Mapper mapper;
    private final ServiceUtil serviceUtil;

    @Transactional(rollbackFor = Exception.class)
    public EventReadOnlyDTO saveEvent(EventCreateDTO createDTO, String userUuid, MultipartFile imageFile) throws AppObjectNotFoundException, IOException, AppObjectInvalidArgumentException, AppServerException {
        try {

            User principalUser = serviceUtil.checkAuthenticationAndReturnPrincipal(userUuid);

            Event eventToSave = mapper.mapToEventEntity(createDTO);
            eventToSave.setOwner(principalUser);
            if (imageFile == null || imageFile.isEmpty()) {
                eventToSave.setImageAttachment(saveDefaultImage());
            } else {
                saveUserImage(imageFile, eventToSave);
            }

            System.out.println(eventToSave.getStatus().name());
            System.out.println(eventToSave.getStatus());
            EventReadOnlyDTO eventReadOnlyDTO = mapper.mapToEventReadOnlyDTO(eventRepository.save(eventToSave));
            LOGGER.info("Event with UUID: {} was successfully saved.", eventReadOnlyDTO.getUuid());
            return eventReadOnlyDTO;
        } catch (AppObjectNotFoundException e) {
            LOGGER.error("Failed to create Event. {}", e.getMessage());
            throw e;
        } catch (AccessDeniedException e) {
            LOGGER.error("Unauthenticated user tried to create event on: {}", LocalDateTime.now());
            throw e;
        } catch (IOException e) {
            LOGGER.warn("Image File was not saved. Transaction Failed. ERROR: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unexpected error while trying to create event. ERROR: {}", e.getMessage());
            throw new AppServerException("An unexpected error occurred. ERROR: " + e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public EventReadOnlyDTO cancelEvent(String eventUuid, String userUuid) throws AppObjectNotFoundException, AccessDeniedException, AppServerException {
        try {
            // Find the event to cancel
            Event eventToCancel = eventRepository.findByUuid(eventUuid)
                    .orElseThrow(() -> new AppObjectNotFoundException("Event", "Event not found"));

            // Check if the user is authorized to cancel the event
            User principalUser = serviceUtil.checkAuthenticationAndReturnPrincipal(userUuid);

            // Check if the current user is the owner of the event
            if (!eventToCancel.getOwner().getUuid().equals(principalUser.getUuid()) && !serviceUtil.isUserAdmin(principalUser)) {
                throw new AccessDeniedException("You do not have permission to cancel this event.");
            }

            // Set the event status to "CANCELED"
            eventToCancel.setStatus(Status.CANCELED);
            eventRepository.save(eventToCancel); // Save the updated event

            // Return the read-only DTO of the updated event
            EventReadOnlyDTO eventReadOnlyDTO = mapper.mapToEventReadOnlyDTO(eventToCancel);
            LOGGER.info("Event with UUID: {} was successfully canceled.", eventReadOnlyDTO.getUuid());
            return eventReadOnlyDTO;
        } catch (AppObjectNotFoundException | AccessDeniedException e) {
            LOGGER.error("Failed to cancel Event. {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unexpected error while trying to cancel event with UUID: {}. ERROR: {}",eventUuid, e.getMessage());
            throw new AppServerException("An unexpected error occurred. ERROR: " + e.getMessage());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public EventReadOnlyDTO attendEvent(EventAttendDTO eventAttendDTO) throws AppObjectNotFoundException, AccessDeniedException, EventFullException, AppObjectUnavailableException, AppObjectInvalidArgumentException, AppServerException {
        try {
            User principalUser = serviceUtil.checkAuthenticationAndReturnPrincipal(eventAttendDTO.getUserUuid());

            Event eventToAttend = eventRepository.findByUuid(eventAttendDTO.getEventUuid())
                    .orElseThrow(() -> new AppObjectNotFoundException("Event", "Event with UUID: " + eventAttendDTO.getEventUuid() + " was not found"));

            if (isEventUnavailable(eventToAttend)) {
                throw new AppObjectUnavailableException("Event", "Event with UUID: " + eventAttendDTO.getEventUuid() + " is unavailable and cannot be attended");
            }

            if (isEventFull(eventToAttend)) {
                throw new EventFullException("Event with UUID: " + eventAttendDTO.getEventUuid() + " is full and cannot be attended");
            }

            if (eventToAttend.getAttendees().contains(principalUser)) {
                throw new AppObjectInvalidArgumentException("User", "User is already attending the event");
            }

            principalUser.getEventsToAttend().add(eventToAttend);
            eventToAttend.setBookedSeats(eventToAttend.getBookedSeats() + 1);

            return mapper.mapToEventReadOnlyDTO(eventToAttend);
        } catch (AppObjectNotFoundException e) {
            LOGGER.error("ERROR While trying to attend event with UUID: {}. ERROR:{}", eventAttendDTO.getEventUuid(), e.getMessage());
            throw e;
        } catch (AccessDeniedException e) {
            LOGGER.error("Unauthenticated user tried to attend event: {}, on: {}", eventAttendDTO.getEventUuid(), LocalDateTime.now());
            throw e;
        } catch (AppObjectUnavailableException | EventFullException e) {
            LOGGER.warn(e.getMessage());
            throw e;
        } catch (AppObjectInvalidArgumentException e) {
            LOGGER.warn("User with UUID: {} tried to attend event more than once", eventAttendDTO.getUserUuid());
            throw e;
        } catch (Exception e) {
            LOGGER.warn("Unexpected Error while trying to attend event with UUID: {}. ERROR: {}", eventAttendDTO.getEventUuid(), e.getMessage());
            throw new AppServerException("An unexpected error occurred. ERROR: " + e.getMessage());
        }
    }

    public Page<EventReadOnlyDTO> getPaginatedEventsByStatus(Status status, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Event> events = eventRepository.findByStatus(status, pageable);


        return events.map(mapper::mapToEventReadOnlyDTO);
    }


    @Transactional
    public Paginated<EventReadOnlyDTO> getPaginatedFilteredEvents(EventFilterDTO filterDTO, int page, int size) {
        // Map the DTO to filters
        EventFilters filters = mapper.mapToEventFilters(filterDTO);
        filters.setPageable(Pageable.ofSize(size).withPage(page));

        // Fetch filtered data
        var filtered = eventRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());

        return new Paginated<>(filtered.map(mapper::mapToEventReadOnlyDTO));
    }

    @Transactional
    public List<EventReadOnlyDTO> getFilteredEvents(EventFilterDTO filterDTO) {
        // Map the DTO to filters
        EventFilters filters = mapper.mapToEventFilters(filterDTO);

        // Fetch filtered data
        return eventRepository.findAll(getSpecsFromFilters(filters))
                .stream().map(mapper::mapToEventReadOnlyDTO).toList();
    }

    private ImageAttachment saveDefaultImage() {
        String uploadDir = "static/";
        String defaultImageName = "NoImageProvided";
        String defaultImageExtension = "svg";
        String defaultImageContentType = "/image/svg";
        return ImageAttachment.builder()
                .fileName(defaultImageName)
                .savedName(defaultImageName)
                .contentType(defaultImageContentType)
                .extension(defaultImageExtension)
                .filePath(Paths.get(uploadDir + defaultImageName).toString())
                .build();
    }

    protected void saveUserImage(MultipartFile imageFile, Event eventToSave) throws IOException, AppObjectInvalidArgumentException, AppServerException {
        String fileName = imageFile.getOriginalFilename();
        String extension = getFileExtension(fileName);
        String contentType = imageFile.getContentType();
        String savedName = UUID.randomUUID() + extension;
        String uploadDir = "C:/tmp/schedulr/img/";
        Path filePath = Paths.get(uploadDir + savedName);

        try {
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new AppObjectInvalidArgumentException("Image", "Image File provided is not an image");
            }

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, imageFile.getBytes());

            ImageAttachment imageAttachment = ImageAttachment.builder()
                    .fileName(fileName)
                    .savedName(savedName)
                    .filePath(filePath.toString())
                    .extension(extension)
                    .contentType(contentType)
                    .build();

            eventToSave.setImageAttachment(imageAttachment);
            LOGGER.info("ImageAttachment with name: {} was saved successfully", imageAttachment.getSavedName());
        } catch (AppObjectInvalidArgumentException e) {
            LOGGER.error(e.getMessage());
            throw e;
        } catch (IOException e) {
            LOGGER.error("Image File could not get saved");
            Files.deleteIfExists(filePath);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unexpected Error while trying to save Image File. ERROR: {}", e.getMessage());
            Files.deleteIfExists(filePath);
            throw new AppServerException("An unexpected error occurred. ERROR: " + e.getMessage());
        }
    }

    private Specification<Event> getSpecsFromFilters(EventFilters filters) {
        return Specification
                .where(EventSpecification.eventDateBetween(filters.getDateFrom(), filters.getDateTo()))
                .and(EventSpecification.eventTitleLike(filters.getTitle()))
                .and(EventSpecification.eventLocationLike(filters.getLocation()))
                .and(EventSpecification.eventPriceBetween(filters.getMinPrice(), filters.getMaxPrice()))
                .and(EventSpecification.eventCategoryIs(filters.getCategory()));
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        } else return "";
    }



    private boolean isEventFull(Event event) {
        return Objects.equals(event.getMaxSeats(), event.getBookedSeats());
    }

    private boolean isEventUnavailable(Event event) {
        return event.getStatus() != Status.PENDING;
    }
}

