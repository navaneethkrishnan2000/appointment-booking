package com.thomasvallen.appointmentbooking.entity;

import com.thomasvallen.appointmentbooking.common.BaseEntity;
import com.thomasvallen.appointmentbooking.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "events")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Event extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Instant eventDateTime;

    @Column(nullable = false)
    private String zoomLink;

    @Enumerated(EnumType.STRING)
    private EventStatus status;
}
