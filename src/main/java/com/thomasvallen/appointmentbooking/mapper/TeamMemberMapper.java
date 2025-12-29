package com.thomasvallen.appointmentbooking.mapper;

import com.thomasvallen.appointmentbooking.dto.response.TeamMemberResponse;
import com.thomasvallen.appointmentbooking.entity.TeamMember;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class TeamMemberMapper {

    public TeamMemberResponse mapToResponse(@NotNull TeamMember member) {

        return TeamMemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .designation(member.getDesignation())
                .description(member.getDescription())
                .profileImage(member.getProfileImage())
                .memberType(member.getMemberType())
                .build();
    }
}
