package com.thomasvallen.appointmentbooking.service.staff;

import com.thomasvallen.appointmentbooking.dto.response.StaffResponse;
import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService implements IStaffService {

    private final UserRepository userRepository;

    public StaffService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponse<List<StaffResponse>> getAllStaffs() {
        return null;
    }

    @Override
    public ApiResponse<StaffResponse> getStaffById(Long adminId) {
        return null;
    }
}
