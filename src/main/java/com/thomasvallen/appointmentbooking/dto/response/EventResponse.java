package com.thomasvallen.appointmentbooking.dto.response;

import com.thomasvallen.appointmentbooking.enums.EventStatus;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {

    private Long id;
    private String title;
    private String description;
    private String zoomLink;
    private Instant dateTime;
    private EventStatus status;
}
