package com.thomasvallen.appointmentbooking.dto.projections;

import com.thomasvallen.appointmentbooking.enums.Role;

public interface UserSecurityProjection {
    Long getUserId();
    String getEmail();
    Role getRole();
}

