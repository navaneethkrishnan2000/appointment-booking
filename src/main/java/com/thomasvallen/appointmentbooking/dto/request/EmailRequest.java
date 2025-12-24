package com.thomasvallen.appointmentbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {

    private String subject;

    @NotBlank(message = "Name cannot be null or blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Pattern(
            regexp = "^(?!.*\\.\\.)(?!.*@.*@)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "Invalid email format"
    )
    private String email; // email of the person sending the contact request

    @Pattern(
            regexp = "^(\\+\\d{1,3})?\\s?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$",
            message = "Invalid phone number format. Phone number must include a '+' sign, country code, and 10 digits."
    )
    private String phoneNumber;

    @NotBlank(message = "Description cannot be null or blank")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}
