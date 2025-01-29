package gr.myprojects.schedulr.security;

import gr.myprojects.schedulr.authentication.JwtAuthenticationFilter;
import gr.myprojects.schedulr.core.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // This implements configuration settings for the authorization
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(myCustomAuthenticationEntryPoint())
                        .accessDeniedHandler(myCustomAccessDeniedHandler()))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/webjars/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/events/**").hasAnyAuthority(Role.SIMPLE_USER.name(), Role.ADMIN.name())
                        .requestMatchers("/api/admin/**").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/comments/**").hasAnyAuthority(Role.SIMPLE_USER.name(), Role.ADMIN.name())
                        .requestMatchers("/**").permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Defines who can access our endpoints
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Who allows to hit my APIs, this is the front end origin point at production level
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:5273"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);    // Got Authorization header so I got credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", corsConfiguration); // Cors configuration for all endpoints
        return source;
    }

    private final UserDetailsService userDetailsService;

    // Here we say that the auth provider we got it's a dao auth provider
    // We could have more than this
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint myCustomAuthenticationEntryPoint() {        // This can be done sice myCustom implements AuthenticationEntry
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler myCustomAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }


//    @Bean
//    public MultipartResolver multipartResolver() {
//        return new StandardServletMultipartResolver();
//    }
}