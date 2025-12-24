package com.thomasvallen.appointmentbooking.service.staff;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.common.utils.PasswordGenerator;
import com.thomasvallen.appointmentbooking.common.utils.SecurityUtil;
import com.thomasvallen.appointmentbooking.common.utils.ValidationUtil;
import com.thomasvallen.appointmentbooking.dto.projections.UserSecurityProjection;
import com.thomasvallen.appointmentbooking.dto.request.AddStaffRequest;
import com.thomasvallen.appointmentbooking.dto.response.StaffResponse;
import com.thomasvallen.appointmentbooking.entity.User;
import com.thomasvallen.appointmentbooking.entity.UserProfile;
import com.thomasvallen.appointmentbooking.enums.AccountStatus;
import com.thomasvallen.appointmentbooking.enums.Role;
import com.thomasvallen.appointmentbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminStaffService implements IAdminStaffService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<StaffResponse> addStaff(AddStaffRequest request) {

        UserSecurityProjection currentSuperAdmin = SecurityUtil.getCurrentUser();

        if (!Objects.equals(currentSuperAdmin.getRole(), Role.SUPER_ADMIN)) {
            return ApiResponse.forbidden("Only SUPER_ADMIN can add staff");
        }

        String email = request.email().trim().toLowerCase();

        if (!ValidationUtil.isEmailValid(email)) {
            return ApiResponse.badRequest("Invalid email format");
        }

        if (userRepository.existsByEmail(email)) {
            return ApiResponse.conflict("Email already registered. Please use a different email.");
        }

        String generatedPassword = PasswordGenerator.generatePassword(8);
        if (!ValidationUtil.isPasswordValid(generatedPassword)) {
            return ApiResponse.badRequest(
                    "Password must contain at least 8 characters, including uppercase, lowercase, number, and special character."
            );
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(generatedPassword))
                .role(Role.STAFF)
                .accountStatus(AccountStatus.ACTIVE)
                .isVerified(true)
                .createdByAdminId(currentSuperAdmin.getUserId())
                .build();

        UserProfile profile = UserProfile.builder()
                .user(user)
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .department(request.department())
                .designation(request.designation())
                .isProfileComplete(false)
                .build();

        user.setUserProfile(profile);
        User savedUser = userRepository.save(user);

        return ApiResponse.success(
                StaffResponse.builder()
                        .id(savedUser.getId())
                        .name(profile.getName())
                        .email(savedUser.getEmail())
                        .role(savedUser.getRole())
                        .accountStatus(savedUser.getAccountStatus())
                        .department(profile.getDepartment())
                        .designation(profile.getDesignation())
                        .isVerified(savedUser.getIsVerified())
                        .isProfileComplete(profile.getIsProfileComplete())
                        .lastLogin(savedUser.getLastLogin())
                        .createdByAdminId(savedUser.getCreatedByAdminId())
                        .updatedByAdminId(savedUser.getUpdatedByAdminId())
                        .createdAt(savedUser.getCreatedAt())
                        .updatedAt(savedUser.getUpdatedAt())
                        .build(),
                "Staff created successfully"
        );
    }
}
