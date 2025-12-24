package com.thomasvallen.appointmentbooking.service.form;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.ConsultationFormRequest;
import com.thomasvallen.appointmentbooking.dto.request.EmailRequest;
import jakarta.validation.Valid;

public interface IFormService {

    ApiResponse<String> sendEmailForContactUs(@Valid EmailRequest request) ;

    ApiResponse<String> saveConsultationForm(@Valid ConsultationFormRequest request);
}
