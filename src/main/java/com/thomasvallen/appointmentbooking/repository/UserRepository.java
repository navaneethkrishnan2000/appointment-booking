package com.thomasvallen.appointmentbooking.repository;

import com.thomasvallen.appointmentbooking.dto.projections.UserSecurityProjection;
import com.thomasvallen.appointmentbooking.entity.User;
import com.thomasvallen.appointmentbooking.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
            SELECT
                u.id AS userId,
                u.email AS email,
                u.role AS role
            FROM User u
            WHERE u.email = :email
            """)
    Optional<UserSecurityProjection> findByEmailWithProjection(@Param("email") String email);

    boolean existsByRole(Role role);

    @Modifying
    @Query("""
               update User u
               set u.lastLogin = :lastLogin
               where u.id = :userId
            """)
    void updateLastLogin(@Param("userId") Long userId,
                         @Param("lastLogin") Instant lastLogin);

    Page<User> findByRole(Role role, Pageable pageable);

}
