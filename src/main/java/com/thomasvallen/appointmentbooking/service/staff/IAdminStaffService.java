package com.thomasvallen.appointmentbooking.service.staff;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.AddStaffRequest;
import com.thomasvallen.appointmentbooking.dto.response.StaffResponse;
import jakarta.validation.Valid;

public interface IAdminStaffService {

    ApiResponse<StaffResponse> addStaff(@Valid AddStaffRequest request);
}
