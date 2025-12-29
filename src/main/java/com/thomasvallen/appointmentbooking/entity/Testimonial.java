package com.thomasvallen.appointmentbooking.entity;

import com.thomasvallen.appointmentbooking.common.BaseEntity;
import com.thomasvallen.appointmentbooking.enums.TestimonialStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "testimonials")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Testimonial extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestimonialStatus status; // PENDING, APPROVED, REJECTED

    @Lob
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    private String profileImageContentType;

    private String profileImageName;

}
