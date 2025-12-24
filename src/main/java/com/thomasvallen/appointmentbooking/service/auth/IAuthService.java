package com.thomasvallen.appointmentbooking.service.auth;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.StaffLoginRequest;
import com.thomasvallen.appointmentbooking.dto.response.StaffLoginResponse;
import jakarta.validation.Valid;

public interface IAuthService {

    ApiResponse<StaffLoginResponse> login(@Valid StaffLoginRequest request);

    ApiResponse<Void> logout(String token);
}
