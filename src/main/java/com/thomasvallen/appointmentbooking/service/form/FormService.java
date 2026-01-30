package com.thomasvallen.appointmentbooking.service.form;

import com.thomasvallen.appointmentbooking.common.exceptions.EmailSendException;
import com.thomasvallen.appointmentbooking.common.exceptions.FileValidationException;
import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.ConsultationFormRequest;
import com.thomasvallen.appointmentbooking.dto.request.EmailRequest;
import com.thomasvallen.appointmentbooking.entity.ConsultationForm;
import com.thomasvallen.appointmentbooking.repository.ConsultationFormRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormService implements IFormService {

    private final JavaMailSender javaMailSender;
    private final ConsultationFormRepository consultationFormRepository;

    private static final String SUBJECT = "Contact Us Information";

    private static final String THOMAS_V_ALLEN_EMAIL_ID = "intake@thomasvallen.com";

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024L;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "image/png",
            "image/jpeg"
    );

    @Override // SMTP
    @Async
    public void sendEmailForContactUs(@NotNull EmailRequest request) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(request.getEmail());
            helper.setTo(THOMAS_V_ALLEN_EMAIL_ID);
            helper.setSubject(request.getSubject());
            helper.setText(buildHtmlEmailTemplate(request), true);

            javaMailSender.send(mimeMessage);

            log.info("Contact-us email sent from {}", request.getEmail());

        } catch (MailException | MessagingException ex) {
            log.error("Failed to send contact-us email from {}",
                    request.getEmail(), ex);

            throw new EmailSendException("Unable to send email at the moment");
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> saveConsultationForm(@NotNull ConsultationFormRequest request) {

        validateFile(request.getFile());

        try {
            ConsultationForm form = ConsultationForm.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .description(request.getDescription())
                    .visaCategory(request.getVisaCategory())
                    .build();

            MultipartFile file = request.getFile();
            if (file != null && !file.isEmpty()) {
                form.setFileData(file.getBytes());
                form.setFileName(file.getOriginalFilename());
                form.setFileType(file.getContentType());
                form.setFileSize(file.getSize());
            }

            consultationFormRepository.save(form);

            return ApiResponse.success(
                    "Consultation request submitted successfully. Our team will contact you soon."
            );

        } catch (IOException ex) {
            log.error("File processing failed for {}", request.getEmail(), ex);
            throw new FileValidationException("Unable to process uploaded file");
        }
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return;
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileValidationException("File size must not exceed 5MB");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new FileValidationException("Only PDF, DOC, DOCX,  JPEG, and PNG files are allowed");
        }
    }

    @NotNull
    private String buildConsultationEmailTemplate(@NotNull ConsultationFormRequest request) {
        return """
        <html>
        <body>
            <h2>New Consultation Request</h2>
            <p><b>Name:</b> %s</p>
            <p><b>Email:</b> %s</p>
            <p><b>Phone:</b> %s</p>
            <p><b>Visa Category:</b> %s</p>
            <p><b>Description:</b></p>
            <p>%s</p>
        </body>
        </html>
        """.formatted(
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getVisaCategory(),
                request.getDescription()
        );
    }

    @NotNull
    private String buildHtmlEmailTemplate(@NotNull EmailRequest emailRequest) {
        return String.format(
                """
                    <div
                        style="
                            max-width: 600px;
                            margin: auto;
                            padding: 32px;
                            border-radius: 12px;
                            background: linear-gradient(135deg, #f8fafc, #e2e8f0);
                            color: #1e293b;
                            font-family: 'Inter', sans-serif;
                            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
                            border: 1px solid rgba(0, 0, 0, 0.1);
                          "
                    >
                        <h2
                            style="
                              color: #2563eb;
                              font-size: 24px;
                              margin-bottom: 24px;
                              font-weight: 600;
                              text-align: center;
                            "
                        >
                            New Contact Form Submission
                        </h2>
                        <div style="margin-bottom: 24px; color: #1e293b">
                            <p style="margin: 12px 0; font-size: 16px"><strong>Name:</strong> %s</p>
                            <p style="margin: 12px 0; font-size: 16px"><strong>Email:</strong> %s</p>
                            <p style="margin: 12px 0; font-size: 16px"><strong>Phone:</strong> %s</p>
                        </div>
                        <p style="font-size: 16px; font-weight: 600; margin-bottom: 12px; color: #2563eb;">
                            Message:
                        </p>
                            <div
                                style="
                                    padding: 16px;
                                    margin: 12px 0;
                                    background: #ffffff;
                                    border-radius: 8px;
                                    color: #1e293b;
                                    font-size: 15px;
                                    line-height: 1.6;
                                    border: 1px solid rgba(0, 0, 0, 0.05);
                                    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
                                "
                            >
                                %s
                            </div>
                            <p
                                style="
                                    color: #64748b;
                                    font-size: 14px;
                                    text-align: center;
                                    margin-top: 24px;
                                "
                                >
                                    This email was sent via the <strong>Thomas V Allen</strong> Contact Us form.
                            </p>
                    </div>
                """,
                emailRequest.getName(),
                emailRequest.getEmail(),
                emailRequest.getPhoneNumber(),
                emailRequest.getDescription()
        );
    }
}
