package com.thomasvallen.appointmentbooking.service.staff;

import com.thomasvallen.appointmentbooking.dto.request.AddStaffRequest;
import com.thomasvallen.appointmentbooking.dto.request.UpdateStaffRequest;
import com.thomasvallen.appointmentbooking.dto.response.StaffResponse;
import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface IStaffService {

    ApiResponse<StaffResponse> addStaff(@Valid AddStaffRequest request);

    ApiResponse<List<StaffResponse>> getAllStaffs(String sortBy, String paginationToken, Integer pageSize, String direction);

    ApiResponse<StaffResponse> getStaffById(Long staffId);

    ApiResponse<StaffResponse> updateStaff(long staffId, @Valid UpdateStaffRequest request);
}

