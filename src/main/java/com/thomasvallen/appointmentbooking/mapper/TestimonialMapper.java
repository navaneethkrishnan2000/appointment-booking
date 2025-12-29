package com.thomasvallen.appointmentbooking.mapper;

import com.thomasvallen.appointmentbooking.dto.response.TestimonialResponse;
import com.thomasvallen.appointmentbooking.entity.Testimonial;
import org.springframework.stereotype.Component;

@Component
public class TestimonialMapper {

    public TestimonialResponse mapToTestimonialResponse(Testimonial t) {
        return TestimonialResponse.builder()
                .id(t.getId())
                .name(t.getName())
                .designation(t.getDesignation())
                .message(t.getMessage())
                .status(t.getStatus())
                .profileImage(t.getProfileImage())
                .build();
    }
}
