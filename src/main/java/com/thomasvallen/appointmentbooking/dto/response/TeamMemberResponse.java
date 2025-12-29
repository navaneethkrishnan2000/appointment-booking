package com.thomasvallen.appointmentbooking.dto.response;

import com.thomasvallen.appointmentbooking.enums.TeamMemberType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberResponse {

    private Long id;
    private String name;
    private String designation;
    private String description;
    private byte[] profileImage;
    private TeamMemberType memberType;
}
