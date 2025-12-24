package com.thomasvallen.appointmentbooking.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtAuthenticationFilter(JwtService jwtService, ObjectMapper objectMapper, UserDetailsService userDetailsService, TokenBlacklistService tokenBlacklistService) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
        this.userDetailsService = userDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException
    {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt;
        final String userEmail;
        final String userRole;

        jwt = authHeader.substring(7);

        if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
            log.warn("Blacklisted token attempted to access: {}", request.getRequestURI());
        }

        try {
            userEmail = jwtService.extractEmail(jwt);
            userRole = jwtService.extractRole(jwt);
            log.debug("Extracted email: {}, role: {} from JWT", userEmail, userRole);
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
            sendErrorResponse(response, "JWT token has expired. Please use refresh token.", HttpStatus.UNAUTHORIZED);
            return;
        } catch (Exception e) {
            log.warn("JWT parsing failed: {}", e.getMessage());
            sendErrorResponse(response, "Invalid JWT token.", HttpStatus.UNAUTHORIZED);
            return;
        }


        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                log.debug("Loaded user details for email: {}", userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority("ROLE_" + userRole);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    Collections.singletonList(authority)
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    log.warn("JWT token invalid or tampered for user: {}", userEmail);
                    sendErrorResponse(response, "JWT token is invalid or tampered.", HttpStatus.UNAUTHORIZED);
                    return;
                }
            } catch (EntityNotFoundException e) {
                log.warn("User not found for JWT email: {}", userEmail);
                sendErrorResponse(response, "User not found for the provided JWT token.", HttpStatus.UNAUTHORIZED);
                return;

            } catch (Exception e) {
                log.error("Unexpected error during JWT authentication: {}", e.getMessage(), e);
                sendErrorResponse(response, "Invalid JWT token.", HttpStatus.UNAUTHORIZED);
                return;
            }

        } else if (userEmail == null) {
            sendErrorResponse(response, "JWT token does not contain a valid email.", HttpStatus.UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(
            HttpServletResponse response,
            String message,
            HttpStatus status
    ) {
        try {
            SecurityContextHolder.clearContext();

            ApiResponse apiResponse = ApiResponse.builder()
                    .data(null)
                    .success(false)
                    .message(message)
                    .status(status)
                    .timestamp(Instant.now())
                    .build();

            response.setStatus(status.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        } catch (IOException ex) {
            log.error("Failed to send error response: {}", ex.getMessage(), ex);
        }
    }
}
