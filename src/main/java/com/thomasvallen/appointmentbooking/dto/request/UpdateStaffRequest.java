package com.thomasvallen.appointmentbooking.dto.request;

import com.thomasvallen.appointmentbooking.common.validator.TestimonialImageValidator.ValidImageFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStaffRequest {

    private String name;
    private String phoneNumber;
    private String department;
    private String designation;
    private String bio;

    @ValidImageFile(
            maxSize = 2 * 1024 * 1024, // 2MB
            message = "Profile image must be JPG, JPEG, PNG or WEBP and ≤ 2MB"
    )
    private MultipartFile profileImage;

}
