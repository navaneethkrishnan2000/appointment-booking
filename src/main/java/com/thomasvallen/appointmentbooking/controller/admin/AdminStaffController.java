package com.thomasvallen.appointmentbooking.controller.admin;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.AddStaffRequest;
import com.thomasvallen.appointmentbooking.dto.response.StaffResponse;
import com.thomasvallen.appointmentbooking.service.staff.IAdminStaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth/super-admin")
@RequiredArgsConstructor
public class AdminStaffController {

    private final IAdminStaffService adminStaffService;

    @PostMapping("/add/staff")
    public ResponseEntity<ApiResponse<StaffResponse>> addStaff(
            @Valid @RequestBody AddStaffRequest request
    ) {
        ApiResponse<StaffResponse> response = adminStaffService.addStaff(request);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
