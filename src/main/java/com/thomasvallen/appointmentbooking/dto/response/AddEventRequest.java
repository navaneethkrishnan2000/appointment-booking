package com.thomasvallen.appointmentbooking.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddEventRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Zoom link is required")
    private String zoomLink;

    @NotNull(message = "Event date & time is required")
    private Instant eventDateTime;
}
