package com.thomasvallen.appointmentbooking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {

    private String title;
    private String description;
    private String zoomLink;
    private Instant eventDateTime;
}
