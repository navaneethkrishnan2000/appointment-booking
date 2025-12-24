package com.thomasvallen.appointmentbooking.entity;

import com.thomasvallen.appointmentbooking.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String phoneNumber;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isProfileComplete = false;

    private String department;

    private String designation;

    private String bio;

    private String profilePhotoUrl;
}
