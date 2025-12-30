package com.thomasvallen.appointmentbooking.controller.staff;

import com.thomasvallen.appointmentbooking.service.staff.IStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/admin/staff")
@RequiredArgsConstructor
public class StaffController {

    private final IStaffService staffService;

}
