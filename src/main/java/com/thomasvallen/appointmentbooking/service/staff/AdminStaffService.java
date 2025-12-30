package com.thomasvallen.appointmentbooking.service.staff;

import com.thomasvallen.appointmentbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminStaffService implements IAdminStaffService {

    private final UserRepository userRepository;

}
