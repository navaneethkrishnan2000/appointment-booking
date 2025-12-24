package com.thomasvallen.appointmentbooking.dto.request;

public record StaffLoginRequest(
        String email,
        String password
) {
}
