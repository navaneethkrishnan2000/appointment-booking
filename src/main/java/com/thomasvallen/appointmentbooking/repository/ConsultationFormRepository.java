package com.thomasvallen.appointmentbooking.repository;

import com.thomasvallen.appointmentbooking.entity.ConsultationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationFormRepository extends JpaRepository<ConsultationForm, Long> {
}
