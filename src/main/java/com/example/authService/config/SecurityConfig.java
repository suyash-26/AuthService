package com.example.authService.config;


import com.example.authService.security.CustomUserDetailsService;
import com.example.authService.security.JwtAuthenticationFilter;
import com.example.authService.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// @EnableMethodSecurity is what makes @PreAuthorize on controller methods actually get
// enforced — without it, an annotation like @PreAuthorize("hasRole('SUPER_ADMIN')") is
// silently ignored and every authenticated user can call the endpoint. This was missing
// and left the role-update endpoint open to any logged-in user, including self-escalation.
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtService jwtService,
                                                   CustomUserDetailsService userDetailsService) throws Exception {

        // Not registered as a @Component/@Bean on purpose: Spring Boot auto-registers any
        // Filter bean into the raw servlet container chain (via FilterRegistrationBean),
        // which would run this filter a second time outside Spring Security's control.
        // Instantiating it here keeps it registered exactly once, at the position we choose below.
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtService, userDetailsService);

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // CSRF protection is for cookie/session-based browser clients; not needed for a
                // stateless, token-authenticated REST API (no session cookie for an attacker to ride on).
                .csrf(csrf -> csrf.disable())
                // Allows the H2 console (served in a frame) to render; H2 console is dev/local only.
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                // No server-side session is created/used — every request must carry its own JWT.
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints: register/login can't require a token you don't have yet,
                        // and the H2 console is a dev tool, not an app endpoint.
                        .requestMatchers("/h2-console/**", "/auth/register", "/auth/login").permitAll()
                        // Everything else — including /auth/me and all of /addresses — requires a
                        // valid, authenticated request.
                        .anyRequest().authenticated()
                )
                // Without this, a missing/invalid token on a protected endpoint falls through to
                // Spring's default AccessDeniedHandler and returns 403 (anonymous auth is enabled by
                // default, so the request never looks "unauthenticated" to Spring). This entry point
                // makes that case return the semantically correct 401 instead.
                .exceptionHandling(ex -> ex.authenticationEntryPoint(
                        (request, response, authException) ->
                                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized Error")
                ))
                // Runs our JWT check before Spring's own username/password filter, so that by the
                // time authorization rules above are evaluated, SecurityContextHolder is already
                // populated if the request carried a valid token.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Vite dev server origin for the frontend; add production frontend origin(s) here when deployed.
        config.setAllowedOrigins(List.of("http://localhost:5173","http://localhost:5174","https://pet-management-user-dashboard.vercel.app","https://pet-management-admin-dashboard.vercel.app"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}