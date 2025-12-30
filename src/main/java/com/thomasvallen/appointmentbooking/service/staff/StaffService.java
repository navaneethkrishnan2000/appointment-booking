package com.thomasvallen.appointmentbooking.service.staff;

import com.thomasvallen.appointmentbooking.common.exceptions.ResourceNotFoundException;
import com.thomasvallen.appointmentbooking.common.utils.PasswordGenerator;
import com.thomasvallen.appointmentbooking.common.utils.SecurityUtil;
import com.thomasvallen.appointmentbooking.common.utils.ValidationUtil;
import com.thomasvallen.appointmentbooking.dto.projections.UserSecurityProjection;
import com.thomasvallen.appointmentbooking.dto.request.AddStaffRequest;
import com.thomasvallen.appointmentbooking.dto.request.UpdateStaffRequest;
import com.thomasvallen.appointmentbooking.dto.response.StaffResponse;
import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.response.TestimonialResponse;
import com.thomasvallen.appointmentbooking.entity.User;
import com.thomasvallen.appointmentbooking.entity.UserProfile;
import com.thomasvallen.appointmentbooking.enums.AccountStatus;
import com.thomasvallen.appointmentbooking.enums.Role;
import com.thomasvallen.appointmentbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StaffService implements IStaffService {

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

        User staff = User.builder()
                .email(email)
                .password(passwordEncoder.encode(generatedPassword))
                .role(Role.STAFF)
                .accountStatus(AccountStatus.ACTIVE)
                .isVerified(true)
                .createdByAdminId(currentSuperAdmin.getUserId())
                .build();

        UserProfile profile = UserProfile.builder()
                .user(staff)
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .department(request.department())
                .designation(request.designation())
                .isProfileComplete(false)
                .build();

        staff.setUserProfile(profile);
        staff = userRepository.save(staff);

        return ApiResponse.created(
                mapToResponse(staff, staff.getUserProfile()),
                "Staff created successfully"
        );
    }

    @Override
    public ApiResponse<List<StaffResponse>> getAllStaffs(
            String sortBy,
            String paginationToken,
            Integer pageSize,
            String direction
    ) {
        Map<String, Object> metadata = new HashMap<>();

        int limit = (pageSize != null && pageSize > 0) ? pageSize : 10;
        int offset = 0;

        if (paginationToken != null && !paginationToken.isEmpty()) {
            // Decode the token which now contains both current and previous offsets
            String decodedToken = new String(Base64.getDecoder().decode(paginationToken));
            String[] offsetValues = decodedToken.split(":");

            if ("prev".equals(direction) && offsetValues.length > 1) {
                // If going backward, use the previous offset
                offset = Integer.parseInt(offsetValues[1]);
            } else {
                // If going forward or direction not specified, use the current offset
                offset = Integer.parseInt(offsetValues[0]);
            }
        }

        int pageNumber = offset / limit;

        Sort sort = "asc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, limit, sort);

        Page<User> staffPage = userRepository.findByRole(Role.STAFF, pageable);

        if (staffPage.isEmpty()) {
            return new ApiResponse<>(
                    Collections.emptyList(),
                    false,
                    "No staffs found",
                    Instant.now(),
                    HttpStatus.NO_CONTENT,
                    metadata
            );
        }

        // Generate next pagination token
        int nextOffset = offset + limit;

        // Create a token that contains both current and previous offsets
        // Format: currentOffset:previousOffset
        String nextPaginationToken = Base64.getEncoder().encodeToString(
                (nextOffset + ":" + offset).getBytes()
        );

        // Generate previous pagination token (if we're not on the first page)
        String prevPaginationToken = null;
        if (offset >= limit) {
            int prevOffset = Math.max(0, offset - limit);
            prevPaginationToken = Base64.getEncoder().encodeToString(
                    (offset + ":" +prevOffset).getBytes()
            );
        }

        metadata.put("nextPaginationToken", nextPaginationToken);
        metadata.put("prevPaginationToken", prevPaginationToken);
        metadata.put("hasMore", staffPage.hasNext());
        metadata.put("hasPrevious", offset > 0);

        metadata.put("currentPage", staffPage.getNumber());
        metadata.put("pageSize", staffPage.getSize());
        metadata.put("totalItems", staffPage.getTotalElements());
        metadata.put("totalPages", staffPage.getTotalPages());

        List<StaffResponse> staffResponses =
                staffPage.getContent().stream()
                        .map(staff -> this.mapToResponse(staff, staff.getUserProfile()))
                        .toList();

        return ApiResponse.success(
                staffResponses,
                "Staffs fetched successfully",
                metadata
        );
    }

    @Override
    public ApiResponse<StaffResponse> getStaffById(Long staffId) {
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

        return ApiResponse.success(
                mapToResponse(staff, staff.getUserProfile()),
                "Staff fetched successfully"
        );
    }

    @Override
    public ApiResponse<StaffResponse> updateStaff(long staffId, UpdateStaffRequest request) {

        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

        if (staff.getRole() != Role.STAFF) {
            return ApiResponse.badRequest("Only staff users can be updated");
        }

        if (staff.isDeactivated()) {
            return ApiResponse.badRequest("Deactivated staff cannot be updated");
        }

        UserProfile profile = staff.getUserProfile();

        if (request.getName() != null)
            profile.setName(request.getName());

        if (request.getPhoneNumber() != null)
            profile.setPhoneNumber(request.getPhoneNumber());

        if (request.getDepartment() != null)
            profile.setDepartment(request.getDepartment());

        if (request.getDesignation() != null)
            profile.setDesignation(request.getDesignation());

        if (request.getBio() != null)
            profile.setBio(request.getBio());

        try {
            if (request.getProfileImage() != null)
                profile.setProfileImage(request.getProfileImage().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Profile completeness check
        profile.setIsProfileComplete(
                profile.getName() != null && profile.getPhoneNumber() != null
        );

        staff.setUpdatedByAdminId(SecurityUtil.getCurrentUserId()); // if available


        staff = userRepository.save(staff);

        return ApiResponse.success(
                mapToResponse(staff, staff.getUserProfile()),
                "Staff updated successfully"
        );
    }


    private StaffResponse mapToResponse(User staff, UserProfile profile) {
        return StaffResponse.builder()
                .id(staff.getId())
                .name(profile.getName())
                .email(staff.getEmail())
                .role(staff.getRole())
                .accountStatus(staff.getAccountStatus())
                .department(profile.getDepartment())
                .designation(profile.getDesignation())
                .isVerified(staff.getIsVerified())
                .isProfileComplete(profile.getIsProfileComplete())
                .lastLogin(staff.getLastLogin())
                .createdByAdminId(staff.getCreatedByAdminId())
                .build();
    }
}
