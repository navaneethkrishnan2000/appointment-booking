package com.thomasvallen.appointmentbooking.controller.team_member;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.AddTeamMemberRequest;
import com.thomasvallen.appointmentbooking.dto.request.UpdateTeamMemberRequest;
import com.thomasvallen.appointmentbooking.dto.response.TeamMemberResponse;
import com.thomasvallen.appointmentbooking.enums.TeamMemberType;
import com.thomasvallen.appointmentbooking.service.team_member.ITeamMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/team/member")
@RequiredArgsConstructor
public class TeamMemberController {

    private final ITeamMemberService teamMemberService;

    @PostMapping(
            value = "/add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<TeamMemberResponse>> add(
            @Valid @ModelAttribute AddTeamMemberRequest request
    ) {
        ApiResponse<TeamMemberResponse> response = teamMemberService.add(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<TeamMemberResponse>> getById(
            @PathVariable long id
    ) {
        ApiResponse<TeamMemberResponse> response = teamMemberService.getById(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<Map<TeamMemberType,List<TeamMemberResponse>>>> getAll(

    ) {
        ApiResponse<Map<TeamMemberType,List<TeamMemberResponse>>> response = teamMemberService.getAll();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping(
            value = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<TeamMemberResponse>> update(
            @PathVariable long id,
            @Valid @ModelAttribute UpdateTeamMemberRequest request
    ) {
        ApiResponse<TeamMemberResponse> response = teamMemberService.update(id, request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable long id
    ) {
        ApiResponse<Void> response = teamMemberService.delete(id);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
