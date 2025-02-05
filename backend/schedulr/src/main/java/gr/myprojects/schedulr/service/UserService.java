package gr.myprojects.schedulr.service;

import gr.myprojects.schedulr.core.enums.Role;
import gr.myprojects.schedulr.core.exceptions.AppObjectAlreadyExistsException;
import gr.myprojects.schedulr.core.exceptions.AppObjectNotFoundException;
import gr.myprojects.schedulr.core.exceptions.AppServerException;
import gr.myprojects.schedulr.core.mapper.Mapper;
import gr.myprojects.schedulr.core.util.ServiceUtil;
import gr.myprojects.schedulr.dto.user.UserReadOnlyDTO;
import gr.myprojects.schedulr.dto.user.UserRegisterDTO;
import gr.myprojects.schedulr.model.User;
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

    @Transactional(rollbackFor = Exception.class)
    public UserReadOnlyDTO deleteUser(String adminUserUuid, String userToDeleteUuid) throws AccessDeniedException, AppObjectNotFoundException, AppServerException {
        try {
            User principalUser = serviceUtil.checkAuthenticationAndReturnPrincipal(adminUserUuid);

            if (userRepository.findByUuid(adminUserUuid).isEmpty()) {
                throw new AppObjectNotFoundException("User", "User with UUID: " + adminUserUuid + " does not exist");
            }


            User userToDelete = userRepository.findByUuid(userToDeleteUuid).orElseThrow(() -> new AppObjectNotFoundException("User", "User with UUID: " + userToDeleteUuid + " was not found"));

            if (!principalUser.getRole().equals(Role.ADMIN)) {
                throw new AccessDeniedException("Access denied while trying to delete user with UUID: " + userToDeleteUuid);
            }

            userToDelete.setIsDeleted(true);
            return mapper.mapToUserReadOnlyDTO(userRepository.save(userToDelete));

        } catch (AppObjectNotFoundException e) {
            LOGGER.warn("Error while trying to delete user with UUID: {}, user was not found", userToDeleteUuid);
            throw e;
        } catch (AccessDeniedException e) {
            LOGGER.warn("Non-ADMIN user tried to delete user with UUID: {}", userToDeleteUuid);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unexpected error while trying to delete user with UUID: {}", userToDeleteUuid);
            throw new AppServerException("Unexpected error while deleting user. ERROR: " + e.getMessage());
        }
    }

}
