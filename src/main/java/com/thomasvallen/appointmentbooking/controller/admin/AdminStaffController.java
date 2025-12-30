package com.thomasvallen.appointmentbooking.controller.admin;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.AddStaffRequest;
import com.thomasvallen.appointmentbooking.dto.request.UpdateStaffRequest;
import com.thomasvallen.appointmentbooking.dto.response.StaffResponse;
import com.thomasvallen.appointmentbooking.service.staff.IStaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/staff")
@RequiredArgsConstructor
public class AdminStaffController {

    private final IStaffService staffService;

    @PostMapping
    public ResponseEntity<ApiResponse<StaffResponse>> addStaff(
            @Valid @RequestBody AddStaffRequest request
    ) {
        ApiResponse<StaffResponse> response = staffService.addStaff(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getAllStaffs(
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false) String paginationToken,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "next") String direction
    ) {
        ApiResponse<List<StaffResponse>> response = staffService.getAllStaffs(sortBy, paginationToken, pageSize, direction);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<ApiResponse<StaffResponse>> getStaffById(
            @PathVariable Long staffId
    ) {
        ApiResponse<StaffResponse> response = staffService.getStaffById(staffId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("/{staffId}")
    public ResponseEntity<ApiResponse<StaffResponse>> updateStaff(
            @PathVariable long staffId,
            @Valid @RequestBody UpdateStaffRequest request
    ) {
        ApiResponse<StaffResponse> response = staffService.updateStaff(staffId, request);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
