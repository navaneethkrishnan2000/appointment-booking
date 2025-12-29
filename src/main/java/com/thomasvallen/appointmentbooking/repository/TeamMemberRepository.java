package com.thomasvallen.appointmentbooking.repository;

import com.thomasvallen.appointmentbooking.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
}
