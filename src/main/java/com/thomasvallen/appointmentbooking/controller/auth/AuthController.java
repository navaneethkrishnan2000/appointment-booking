package com.thomasvallen.appointmentbooking.controller.auth;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.StaffLoginRequest;
import com.thomasvallen.appointmentbooking.dto.response.StaffLoginResponse;
import com.thomasvallen.appointmentbooking.service.auth.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final IAuthService staffAuthService;

    public AuthController(IAuthService staffAuthService) {
        this.staffAuthService = staffAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<StaffLoginResponse>> login (
            @Valid @RequestBody StaffLoginRequest request
    ) {
        ApiResponse<StaffLoginResponse> response = staffAuthService.login(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout (
           HttpServletRequest request
    ) {
        String authHeader = request.getHeader("Authorization");
        ApiResponse<Void> response = staffAuthService.logout(authHeader);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
