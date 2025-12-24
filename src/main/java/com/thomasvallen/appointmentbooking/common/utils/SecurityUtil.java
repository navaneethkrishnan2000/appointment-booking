package com.thomasvallen.appointmentbooking.common.utils;

import com.thomasvallen.appointmentbooking.dto.projections.UserSecurityProjection;
import com.thomasvallen.appointmentbooking.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private static UserRepository userRepository;

    public SecurityUtil(UserRepository userRepository) {
        SecurityUtil.userRepository = userRepository;
    }

    /**
     * Retrieves the currently authenticated Employee (Admin, Employee, or SuperAdmin)
     */
    public static UserSecurityProjection getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();
        String email;

        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else if (principal instanceof String str) {
            email = str;
        } else {
            throw new IllegalStateException("Unsupported principal type: " + principal.getClass());
        }

        return userRepository.findByEmailWithProjection(email)
                .orElseThrow(() -> new IllegalStateException("User not found for email: " + email));
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }

    public static String getCurrentUserRole() {
        return getCurrentUser().getRole().name()  ;
    }
}
