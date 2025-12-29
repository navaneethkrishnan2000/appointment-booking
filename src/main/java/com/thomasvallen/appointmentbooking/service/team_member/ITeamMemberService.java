package com.thomasvallen.appointmentbooking.service.team_member;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.AddTeamMemberRequest;
import com.thomasvallen.appointmentbooking.dto.request.UpdateTeamMemberRequest;
import com.thomasvallen.appointmentbooking.dto.response.TeamMemberResponse;
import com.thomasvallen.appointmentbooking.enums.TeamMemberType;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

public interface ITeamMemberService {
    
    ApiResponse<TeamMemberResponse> add(@Valid AddTeamMemberRequest request);

    ApiResponse<TeamMemberResponse> getById(long id);

    ApiResponse<Map<TeamMemberType,List<TeamMemberResponse>>> getAll();

    ApiResponse<TeamMemberResponse> update(long id, @Valid UpdateTeamMemberRequest request);

    ApiResponse<Void> delete(long id);
}
