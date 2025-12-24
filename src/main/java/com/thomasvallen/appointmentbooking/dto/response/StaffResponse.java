package com.thomasvallen.appointmentbooking.dto.response;

import com.thomasvallen.appointmentbooking.enums.AccountStatus;
import com.thomasvallen.appointmentbooking.enums.Role;
import lombok.Builder;

import java.time.Instant;

@Builder
public record StaffResponse(

        Long id,

        String name,

        String email,

        String phoneNumber,

        Role role,

        AccountStatus accountStatus,

        String department,

        String designation,

        Boolean isVerified,

        Boolean isProfileComplete,

        Instant lastLogin,

        Long createdByAdminId,

        Long updatedByAdminId,

        Instant createdAt,

        Instant updatedAt
) {
}
