package com.thomasvallen.appointmentbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationFormRequest {

    @NotBlank(message = "Name cannot be null or blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Pattern(
            regexp = "^(?!.*\\.\\.)(?!.*@.*@)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "Invalid email format"
    )
    private String email;

    @NotBlank(message = "Phone Number cannot be null or blank")
    private String phoneNumber;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Visa category cannot be null or blank")
    private String visaCategory;

    @NotBlank(message = "File cannot be blank")
    private MultipartFile file;
}