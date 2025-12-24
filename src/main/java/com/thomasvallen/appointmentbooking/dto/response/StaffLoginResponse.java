package com.thomasvallen.appointmentbooking.dto.response;

import lombok.Builder;

@Builder
public record StaffLoginResponse(
        String token,
        String email,
        String role
) {
}
