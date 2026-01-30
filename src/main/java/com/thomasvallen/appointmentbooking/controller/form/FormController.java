package com.thomasvallen.appointmentbooking.controller.form;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.ConsultationFormRequest;
import com.thomasvallen.appointmentbooking.dto.request.EmailRequest;
import com.thomasvallen.appointmentbooking.service.form.IFormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/form")
@RequiredArgsConstructor
public class FormController {

    private final IFormService formService;

    @PostMapping("/contact/submit")
    public ResponseEntity<ApiResponse<String>> sendEmailForContactUs(
            @Valid @RequestBody EmailRequest request
    ) {
       formService.sendEmailForContactUs(request);
        return ResponseEntity.ok(
                ApiResponse.success("Message sent successfully. We will contact you soon.")
        );
    }

    @PostMapping(
            value = "/consultation",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<String>> saveConsultationForm(
            @Valid @ModelAttribute ConsultationFormRequest request
    ) {
        ApiResponse<String> response = formService.saveConsultationForm(request);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
