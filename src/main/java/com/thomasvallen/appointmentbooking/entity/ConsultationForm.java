package com.thomasvallen.appointmentbooking.entity;

import com.thomasvallen.appointmentbooking.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(
        name = "consultation_forms",
        indexes = {
                @Index(name = "idx_consultation_email", columnList = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationForm extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String email;

    private String phoneNumber;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, length = 50)
    private String visaCategory;

    @Lob
    @Column(name = "file_data")
    private byte[] fileData;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_type", length = 50)
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;
}
