package com.thomasvallen.appointmentbooking.controller.staff;

import com.thomasvallen.appointmentbooking.dto.response.StaffResponse;
import com.thomasvallen.appointmentbooking.service.staff.IStaffService;
import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final IStaffService staffService;

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getAllStaffs(

    ) {
        ApiResponse<List<StaffResponse>> response = staffService.getAllStaffs();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/get/{staffId}")
    public ResponseEntity<ApiResponse<StaffResponse>> getStaffById(
            @PathVariable("adminId") Long adminId
    ) {
        ApiResponse<StaffResponse> response = staffService.getStaffById(adminId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping("/block/admin")
    public ResponseEntity<ApiResponse<Void>> blockAdmin() {
        return null;
    }


}
