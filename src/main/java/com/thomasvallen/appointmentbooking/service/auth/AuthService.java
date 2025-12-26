package com.thomasvallen.appointmentbooking.service.auth;

import com.thomasvallen.appointmentbooking.common.exceptions.AuthenticationFailedException;
import com.thomasvallen.appointmentbooking.common.exceptions.UserAccountException;
import com.thomasvallen.appointmentbooking.common.exceptions.UserNotVerifiedException;
import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.config.security.JwtService;
import com.thomasvallen.appointmentbooking.config.security.TokenBlacklistService;
import com.thomasvallen.appointmentbooking.config.security.UserPrincipal;
import com.thomasvallen.appointmentbooking.dto.request.StaffLoginRequest;
import com.thomasvallen.appointmentbooking.dto.response.StaffLoginResponse;
import com.thomasvallen.appointmentbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    @Transactional
    public ApiResponse<StaffLoginResponse> login(StaffLoginRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            UserPrincipal principal =
                    (UserPrincipal) authentication.getPrincipal();

            if (!principal.isEnabled()) {
                throw new UserAccountException("User account is not active");
            }

            if (!principal.isVerified()) {
                throw new UserNotVerifiedException("User email is not verified");
            }

            String token = jwtService.generateAccessToken(
                            principal,
                            principal.getRole().name());

            userRepository.updateLastLogin(
                    principal.getUserId(),
                    Instant.now()
            );

            log.info("User logged in successfully: {}", principal.getUsername());


            return ApiResponse.success(
                    StaffLoginResponse.builder()
                    .token(token)
                    .email(principal.getUsername())
                    .role(principal.getRole().name())
                    .build(),
                    "Login Successful"
            );

        } catch (BadCredentialsException e) {
            log.warn("Login failed - Invalid credentials for email: {}", request.email());
            throw new AuthenticationFailedException("Invalid email or password");
        } catch (DisabledException e) {
            log.warn("Login failed - User account disabled: {}", request.email());
            throw new UserAccountException("User account is disabled");
        }
    }

    @Override
    public ApiResponse<Void> logout(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token is required for logout");
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Instant expiry = jwtService.extractExpiration(token).toInstant();

        tokenBlacklistService.blacklist(token, expiry);

        log.info("Token successfully revoked");

        return ApiResponse.success(null, "Logged out successfully");
    }
}
