package gr.myprojects.schedulr.service;

import gr.myprojects.schedulr.core.enums.Role;
import gr.myprojects.schedulr.core.enums.Status;
import gr.myprojects.schedulr.core.exceptions.AppObjectAlreadyExistsException;
import gr.myprojects.schedulr.core.exceptions.AppObjectNotFoundException;
import gr.myprojects.schedulr.core.exceptions.AppServerException;
import gr.myprojects.schedulr.core.mapper.Mapper;
import gr.myprojects.schedulr.core.util.ServiceUtil;
import gr.myprojects.schedulr.dto.event.EventReadOnlyDTO;
import gr.myprojects.schedulr.dto.user.UserReadOnlyDTO;
import gr.myprojects.schedulr.dto.user.UserRegisterDTO;
import gr.myprojects.schedulr.model.Event;
import gr.myprojects.schedulr.model.User;
import gr.myprojects.schedulr.repository.EventRepository;
import gr.myprojects.schedulr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final Mapper mapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final ServiceUtil serviceUtil;

    @Transactional(rollbackFor = Exception.class)
    public UserReadOnlyDTO registerUser(UserRegisterDTO registerDTO) throws AppObjectAlreadyExistsException, AppServerException {
        try {
            if (userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
                throw new AppObjectAlreadyExistsException("User", "User with username "
                        + registerDTO.getUsername() + " already exists");
            }


            if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
                throw new AppObjectAlreadyExistsException("User", "User with email "
                        + registerDTO.getEmail() + " already exists");
            }


            User savedUser = userRepository.save(mapper.mapToUserEntity(registerDTO));

            LOGGER.info("User with username: {} and UUID: {} was saved ", savedUser.getUsername(), savedUser.getUuid());

            return mapper.mapToUserReadOnlyDTO(savedUser);
        } catch (AppObjectAlreadyExistsException e) {
            LOGGER.warn(e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("User was not saved. ERROR: {}", e.getMessage());
            throw new AppServerException("An unexpected error occurred. ERROR: " + e.getMessage());

        }
    }








//    public List<EventReadOnlyDTO> getOwnedEventsByUsername(String username) throws AppObjectNotFoundException, AppServerException {
//        try {
//            if (userRepository.findByUsername(username).isEmpty()) {
//                throw new AppObjectNotFoundException("User", "User with UUID: " + username + " does not exist");
//            }
//
//            List<Event> ownedEventsList = eventRepository.findByOwnerUuid(username);
//            LOGGER.info("Events owned by user with Username: {}, where fetched. Number of events: {}",
//                    username, ownedEventsList.size());
//
//            return mapper.mapToEventReadOnlyDTO(ownedEventsList);
//        } catch (AppObjectNotFoundException e) {
//            LOGGER.warn("Events for user with Username: {} where not fetched. {}", username, e.getMessage());
//            throw e;
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//            throw new AppServerException(e.getMessage());
//        }
//    }


}
