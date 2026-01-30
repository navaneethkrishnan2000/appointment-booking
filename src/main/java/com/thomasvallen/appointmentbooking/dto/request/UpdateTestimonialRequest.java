package com.thomasvallen.appointmentbooking.dto.request;

import com.thomasvallen.appointmentbooking.common.validator.testimonial_image_validator.ValidImageFile;
import com.thomasvallen.appointmentbooking.enums.TestimonialStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTestimonialRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Designation is required")
    private String designation;

    @NotBlank(message = "Message is required")
    private String message;

    @ValidImageFile(
            maxSize = 2 * 1024 * 1024, // 2MB
            message = "Profile image must be JPG, JPEG, PNG or WEBP and ≤ 2MB"
    )
    private MultipartFile profileImage;

    @NotNull(message = "Status is required")
    private TestimonialStatus status;
}
