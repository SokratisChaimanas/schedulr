package gr.myprojects.schedulr.service;

import gr.myprojects.schedulr.core.enums.Role;
import gr.myprojects.schedulr.core.exceptions.AppObjectInvalidArgumentException;
import gr.myprojects.schedulr.core.exceptions.AppObjectNotFoundException;
import gr.myprojects.schedulr.core.exceptions.AppServerException;
import gr.myprojects.schedulr.core.mapper.Mapper;
import gr.myprojects.schedulr.core.util.ServiceUtil;
import gr.myprojects.schedulr.dto.comment.CommentCreateDTO;
import gr.myprojects.schedulr.dto.comment.CommentReadOnlyDTO;
import gr.myprojects.schedulr.model.Comment;
import gr.myprojects.schedulr.model.Event;
import gr.myprojects.schedulr.model.User;
import gr.myprojects.schedulr.repository.CommentRepository;
import gr.myprojects.schedulr.repository.EventRepository;
import gr.myprojects.schedulr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.access.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final ServiceUtil serviceUtil;
    private final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

    @Transactional(rollbackFor = Exception.class)
    public CommentReadOnlyDTO createComment(CommentCreateDTO commentCreateDTO, String userUuid, String eventUuid) throws AppObjectNotFoundException, AppObjectInvalidArgumentException, AppServerException {
        try {
            User principalUser = serviceUtil.checkAuthenticationAndReturnPrincipal(userUuid);

            Event event = eventRepository.findByUuid(eventUuid)
                    .orElseThrow(() -> new AppObjectNotFoundException("Event", "Event with UUID: " + eventUuid + " was not found"));

            Comment commentToSave = mapper.mapToComment(commentCreateDTO);
            commentToSave.setAuthor(principalUser);
            commentToSave.setEvent(event);

            Comment savedComment = commentRepository.save(commentToSave);
            CommentReadOnlyDTO commentReadOnlyDTO = mapper.mapToCommentReadOnlyDTO(savedComment);

            LOGGER.info("Comment with ID: {} was successfully created for Event UUID: {} by User UUID: {}", savedComment.getId(), eventUuid, userUuid);
            return commentReadOnlyDTO;
        } catch (AppObjectNotFoundException e) {
            LOGGER.warn("Failed to create comment. {}", e.getMessage());
            throw e;
        } catch (AccessDeniedException e) {
            LOGGER.error("Comment on event with UUID: {} cannot be created. ERROR: {}", eventUuid, e.getMessage());
            throw new AppServerException("An unexpected error occurred while creating the comment. ERROR: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Unexpected error while creating comment for Event UUID: {} by User UUID: {}. ERROR: {}", eventUuid, userUuid, e.getMessage());
            throw new AppServerException("An unexpected error occurred while creating the comment. ERROR: " + e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public CommentReadOnlyDTO deleteComment(String userUuid, String commentUuid) throws AppObjectNotFoundException, AccessDeniedException, AppServerException {
        try {
            User principalUser = serviceUtil.checkAuthenticationAndReturnPrincipal(userUuid);

            Comment commentToDelete = commentRepository.findByUuid(commentUuid)
                    .orElseThrow(() -> new AppObjectNotFoundException("Comment", "Comment with UUID: " + commentUuid + " was not found"));

            if (!commentToDelete.getAuthor().getUuid().equals(principalUser.getUuid()) && !serviceUtil.isUserAdmin(principalUser)) {
                throw new AccessDeniedException("You do not have permission to delete this comment.");
            }

            commentToDelete.setIsDeleted(true);
            LOGGER.info("Comment with UUID: {} was successfully deleted by User UUID: {}", commentUuid, userUuid);

            return mapper.mapToCommentReadOnlyDTO(commentToDelete);
        } catch (AppObjectNotFoundException e) {
            LOGGER.warn("Failed to delete comment. {}", e.getMessage());
            throw e;
        } catch (AccessDeniedException e) {
            LOGGER.warn("Access denied for User UUID: {} while deleting Comment ID: {}. ERROR: {}", userUuid, commentUuid, e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unexpected error while deleting Comment ID: {} by User UUID: {}. ERROR: {}", commentUuid, userUuid, e.getMessage());
            throw new AppServerException("An unexpected error occurred while deleting the comment");
        }
    }
}
