package gr.myprojects.schedulr.core.util;

import gr.myprojects.schedulr.core.enums.Role;
import gr.myprojects.schedulr.core.exceptions.AppObjectNotFoundException;
import gr.myprojects.schedulr.model.User;
import gr.myprojects.schedulr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class ServiceUtil {
    private final UserRepository userRepository;


    public User checkAuthenticationAndReturnPrincipal(String userUuid) throws AccessDeniedException, AppObjectNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String principalUsername = authentication.getName();
        User principalUser = userRepository.findByUsername(principalUsername)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with username: " + principalUsername + " from ContextHolder is not registered in DB"));

        System.out.println(principalUser.getUuid() + " " + principalUser.getUsername());
        System.out.println(userUuid + " " + userRepository.findByUuid(userUuid).orElseThrow(() -> new AccessDeniedException("no user")).getUsername());
        if (!principalUser.getUuid().equalsIgnoreCase(userUuid)) {
            throw new AccessDeniedException("Uuid missmatch, User is not authorized for this event");
        }

        return principalUser;
    }

    public boolean isUserAdmin(User user) {
        return user != null && user.getRole().equals(Role.ADMIN);
    }
}
