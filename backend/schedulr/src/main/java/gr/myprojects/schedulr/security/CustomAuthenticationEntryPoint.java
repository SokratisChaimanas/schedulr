package gr.myprojects.schedulr.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * Checks authentication exceptions
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
            String json;
        if (authException instanceof BadCredentialsException) {
            // Set the response for Bad Credentials
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            json = "{\"code\": \"InvalidCredentials\", \"description\": \"Invalid username or password.\"}";
        } else {
            // Set the response status for UNAUTHORIZED - 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            json = "{\"code:\": \"UserNotAuthenticated\", \"description\": \"User must authenticate to access this endpoint\"}";
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}