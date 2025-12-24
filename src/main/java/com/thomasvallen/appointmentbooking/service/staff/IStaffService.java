package com.thomasvallen.appointmentbooking.service.staff;

import com.thomasvallen.appointmentbooking.dto.response.StaffResponse;
import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;

import java.util.List;

public interface IStaffService {

    ApiResponse<List<StaffResponse>> getAllStaffs();

    ApiResponse<StaffResponse> getStaffById(Long adminId);
}

