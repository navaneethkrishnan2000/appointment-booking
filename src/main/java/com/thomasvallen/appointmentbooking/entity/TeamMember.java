package com.thomasvallen.appointmentbooking.entity;

import com.thomasvallen.appointmentbooking.common.BaseEntity;
import com.thomasvallen.appointmentbooking.enums.TeamMemberType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team_members")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TeamMember extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String description;

    @Lob
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamMemberType memberType;
}
