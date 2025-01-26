package gr.myprojects.schedulr.core.mapper;

import gr.myprojects.schedulr.core.enums.Role;
import gr.myprojects.schedulr.core.enums.Status;
import gr.myprojects.schedulr.core.filters.EventFilters;
import gr.myprojects.schedulr.dto.comment.CommentCreateDTO;
import gr.myprojects.schedulr.dto.comment.CommentReadOnlyDTO;
import gr.myprojects.schedulr.dto.event.EventCreateDTO;
import gr.myprojects.schedulr.dto.event.EventFilterDTO;
import gr.myprojects.schedulr.dto.event.EventReadOnlyDTO;
import gr.myprojects.schedulr.dto.event.ImageAttachmentReadOnlyDTO;
import gr.myprojects.schedulr.dto.user.UserReadOnlyDTO;
import gr.myprojects.schedulr.dto.user.UserRegisterDTO;
import gr.myprojects.schedulr.model.Comment;
import gr.myprojects.schedulr.model.Event;
import gr.myprojects.schedulr.model.ImageAttachment;
import gr.myprojects.schedulr.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Mapper {

    private final PasswordEncoder passwordEncoder;

    public User mapToUserEntity(UserRegisterDTO registerDTO) {
        return User.builder()
                .username(registerDTO.getUsername())
                .firstname(registerDTO.getFirstname())
                .lastname(registerDTO.getLastname())
                .email(registerDTO.getEmail())
                .role(Role.valueOf(registerDTO.getRole()))
                .isDeleted(false)
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .build();
    }

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        return UserReadOnlyDTO.builder()
                .username(user.getUsername())
                .uuid(user.getUuid())
                .build();
    }

    public List<UserReadOnlyDTO> mapToUserReadOnlyDTO(List<User> userList) {
        List<UserReadOnlyDTO> listToReturn = new ArrayList<>();

        for (User user : userList) {
            UserReadOnlyDTO readOnlyDTO = mapToUserReadOnlyDTO(user);
            listToReturn.add(readOnlyDTO);
        }
        return listToReturn;
    }

    public Event mapToEventEntity(EventCreateDTO eventCreateDTO) {
        return Event.builder()
                .title(eventCreateDTO.getTitle())
                .description(eventCreateDTO.getDescription())
                .date(eventCreateDTO.getDate())
                .location(eventCreateDTO.getLocation())
                .category(eventCreateDTO.getCategory())
                .maxSeats(eventCreateDTO.getMaxSeats())
                .price(eventCreateDTO.getPrice())
                .status(Status.PENDING)
                .bookedSeats(0)
                .build();
    }

    public EventReadOnlyDTO mapToEventReadOnlyDTO(Event event) {
        return EventReadOnlyDTO.builder()
                .status(event.getStatus())
                .ownerReadOnlyDTO(mapToUserReadOnlyDTO(event.getOwner()))
                .imageAttachmentReadOnlyDTO(mapToImageAttachmentReadOnlyDTO(event.getImageAttachment()))
                .category(event.getCategory())
                .title(event.getTitle())
                .description(event.getDescription())
                .date(event.getDate())
                .price(event.getPrice())
                .uuid(event.getUuid())
                .maxSeats(event.getMaxSeats())
                .bookedSeats(event.getBookedSeats())
                .commentsList(mapToCommentReadOnlyDTO(
                        Optional.ofNullable(event.getComments())
                                .orElse(Collections.emptySet())
                                .stream()
                                .filter(comment -> !comment.getIsDeleted()) // Include only comments where isDeleted is false
                                .toList()
                ))
                .build();
    }


    public List<EventReadOnlyDTO> mapToEventReadOnlyDTO(List<Event> eventList) {
        List<EventReadOnlyDTO> listToReturn = new ArrayList<>();

        for (Event event : eventList) {
            EventReadOnlyDTO readOnlyDTO = mapToEventReadOnlyDTO(event);
            listToReturn.add(readOnlyDTO);
        }

        return listToReturn;
    }

    public ImageAttachmentReadOnlyDTO mapToImageAttachmentReadOnlyDTO(ImageAttachment imageAttachment) {
        return ImageAttachmentReadOnlyDTO.builder()
                .filepath(imageAttachment.getFilePath())
                .savedName(imageAttachment.getSavedName())
                .extension(imageAttachment.getExtension())
                .build();
    }

    public EventFilters mapToEventFilters(EventFilterDTO filterDTO) {
        return EventFilters.builder()
                .category(filterDTO.getCategory())
                .dateFrom(filterDTO.getStartDate() != null ? filterDTO.getStartDate().atStartOfDay() : null)
                .dateTo(filterDTO.getEndDate() != null ? filterDTO.getEndDate().atTime(23, 59, 59) : null)
                .location(filterDTO.getLocation())
                .title(null) // Placeholder, if "title" is added to DTO in the future
                .build();
    }

    public Comment mapToComment(CommentCreateDTO createDTO) {
        return Comment.builder()
                .description(createDTO.getDescription())
                .isDeleted(false)
                .build();
    }

    public CommentReadOnlyDTO mapToCommentReadOnlyDTO(Comment comment) {
        return CommentReadOnlyDTO.builder()
                .description(comment.getDescription())
                .date(comment.getTimestamp())
                .uuid(comment.getUuid())
                .authorUsername(comment.getAuthor().getUsername())
                .eventTitle(comment.getEvent().getTitle())
                .eventUuid(comment.getEvent().getUuid())
                .isDeleted(comment.getIsDeleted())
                .build();
    }

    public List<CommentReadOnlyDTO> mapToCommentReadOnlyDTO(List<Comment> commentsList) {
        List<CommentReadOnlyDTO> listToReturn = new ArrayList<>();

        for (Comment comment : commentsList) {
            CommentReadOnlyDTO commentReadOnlyDTO = mapToCommentReadOnlyDTO(comment);
            listToReturn.add(commentReadOnlyDTO);
        }

        return listToReturn;
    }
}
