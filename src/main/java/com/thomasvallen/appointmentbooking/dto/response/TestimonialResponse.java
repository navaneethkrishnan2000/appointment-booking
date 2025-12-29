package com.thomasvallen.appointmentbooking.dto.response;

import com.thomasvallen.appointmentbooking.enums.TestimonialStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestimonialResponse {
    private Long id;
    private String name;
    private String designation;
    private String message;
    private byte[] profileImage;
    private TestimonialStatus status;
}
