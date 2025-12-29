package com.thomasvallen.appointmentbooking.service.team_member;

import com.thomasvallen.appointmentbooking.common.exceptions.ResourceNotFoundException;
import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.AddTeamMemberRequest;
import com.thomasvallen.appointmentbooking.dto.request.UpdateTeamMemberRequest;
import com.thomasvallen.appointmentbooking.dto.response.TeamMemberResponse;
import com.thomasvallen.appointmentbooking.entity.TeamMember;
import com.thomasvallen.appointmentbooking.enums.TeamMemberType;
import com.thomasvallen.appointmentbooking.mapper.TeamMemberMapper;
import com.thomasvallen.appointmentbooking.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamMemberService implements ITeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamMemberMapper mapper;

    @Override
    public ApiResponse<TeamMemberResponse> add(
            @NotNull AddTeamMemberRequest request
    ) {
        TeamMember member = TeamMember.builder()
                .name(request.getName())
                .designation(request.getDesignation())
                .description(request.getDescription())
                .memberType(request.getMemberType())
                .build();

        MultipartFile image = request.getProfileImage();
        if (image != null && !image.isEmpty()) {
            try {
                member.setProfileImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read profile image", e);
            }
        }

        member = teamMemberRepository.save(member);
        return ApiResponse.success(
                mapper.mapToResponse(member),
                "Team member added successfully");
    }

    @Override
    public ApiResponse<TeamMemberResponse> getById(long id) {
        TeamMember member = teamMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found"));

        return ApiResponse.success(mapper.mapToResponse(member));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<Map<TeamMemberType, List<TeamMemberResponse>>> getAll() {
        List<TeamMember> members = teamMemberRepository.findAll();
        if (members.isEmpty()) {
            return ApiResponse.success(Collections.emptyMap());
        }

        LinkedHashMap<TeamMemberType, List<TeamMemberResponse>> groupedResponse = members.stream()
                .map(mapper::mapToResponse)
                .collect(Collectors.groupingBy(
                        TeamMemberResponse::getMemberType,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return ApiResponse.success(groupedResponse);
    }

    @Override
    public ApiResponse<TeamMemberResponse> update(
            long id,
            @NotNull UpdateTeamMemberRequest request
    ) {
        TeamMember member = teamMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found"));

        member.setName(request.getName());
        member.setDesignation(request.getDesignation());
        member.setDescription(request.getDescription());
        member.setMemberType(request.getMemberType());

        MultipartFile image = request.getProfileImage();
        if (image != null && !image.isEmpty()) {
            try {
                member.setProfileImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read profile image", e);
            }
        }

        member = teamMemberRepository.save(member);
        return ApiResponse.success(mapper.mapToResponse(member));
    }

    @Override
    public ApiResponse<Void> delete(long id) {
        TeamMember member = teamMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found"));

        teamMemberRepository.delete(member);
        return ApiResponse.success("Team member deleted successfully");
    }
}
