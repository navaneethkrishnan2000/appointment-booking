package com.thomasvallen.appointmentbooking.dto.request;

import com.thomasvallen.appointmentbooking.common.validator.TestimonialImageValidator.ValidImageFile;
import com.thomasvallen.appointmentbooking.enums.TeamMemberType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class AddTeamMemberRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Designation is required")
    private String designation;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Profile image is required")
    @ValidImageFile(
            maxSize = 2 * 1024 * 1024, // 2MB
            message = "Profile image must be JPG, JPEG, PNG or WEBP and ≤ 2MB"
    )
    private MultipartFile profileImage;

    @NotNull(message = "Member type is required")
    private TeamMemberType memberType;
}
