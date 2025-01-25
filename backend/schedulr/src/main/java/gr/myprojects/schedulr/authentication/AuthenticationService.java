package gr.myprojects.schedulr.authentication;

import gr.myprojects.schedulr.core.exceptions.UserNotAuthorizedException;
import gr.myprojects.schedulr.dto.authentication.AuthenticationRequestDTO;
import gr.myprojects.schedulr.dto.authentication.AuthenticationResponseDTO;
import gr.myprojects.schedulr.model.User;
import gr.myprojects.schedulr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto)
            throws UserNotAuthorizedException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() ->new UserNotAuthorizedException("User not authorized"));

//        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());

        return new AuthenticationResponseDTO(token, user.getUuid(), user.getRole());
    }
}
