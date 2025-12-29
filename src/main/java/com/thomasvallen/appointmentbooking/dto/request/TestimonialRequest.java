package com.thomasvallen.appointmentbooking.dto.request;

import com.thomasvallen.appointmentbooking.common.validator.TestimonialImageValidator.ValidImageFile;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class TestimonialRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Designation is required")
    private String designation;

    @NotBlank(message = "Message is required")
    private String message;

    @Lob
    @ValidImageFile(
            maxSize = 2 * 1024 * 1024, // 2MB
            message = "Profile image must be JPG, JPEG, PNG or WEBP and ≤ 2MB"
    )
    private MultipartFile profileImage;
}
