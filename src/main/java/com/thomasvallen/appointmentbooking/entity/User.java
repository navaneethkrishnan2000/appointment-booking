package com.thomasvallen.appointmentbooking.entity;

import com.thomasvallen.appointmentbooking.common.BaseEntity;
import com.thomasvallen.appointmentbooking.enums.AccountStatus;
import com.thomasvallen.appointmentbooking.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isVerified = false;

    private Instant lastLogin;

    /* ================= ADMIN ACTION TRACKING ================= */

    @Column(name = "created_by_admin_id")
    private Long createdByAdminId;

    @Column(name = "updated_by_admin_id")
    private Long updatedByAdminId;

    private Instant deactivatedAt;

    @Column(name = "deactivated_by_admin_id")
    private Long deactivatedByAdminId;

    /* ================= RELATIONSHIPS ================= */

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private UserProfile userProfile;

    /* ================= HELPER METHODS ================= */

    public boolean isSuperAdmin() {
        return this.role == Role.SUPER_ADMIN;
    }

    public boolean isStaff() {
        return this.role == Role.STAFF;
    }

    public boolean isActive() {
        return this.accountStatus == AccountStatus.ACTIVE;
    }

    public boolean isDeactivated() {
        return this.accountStatus == AccountStatus.DEACTIVATED;
    }

    public void deactivateBySuperAdmin() {
        this.accountStatus = AccountStatus.DEACTIVATED;
        this.deactivatedAt = Instant.now();
    }

    public void reactivateBySuperAdmin(Long superAdminId) {
        this.accountStatus = AccountStatus.ACTIVE;
        this.deactivatedAt = null;
        this.updatedByAdminId = superAdminId;
    }

    /* ================= FACTORY ================= */

    @NotNull
    public static User createSuperAdmin(String email, String encodedPassword) {
        User user = new User();
        user.email = email;
        user.password = encodedPassword;
        user.role = Role.SUPER_ADMIN;
        user.accountStatus = AccountStatus.ACTIVE;
        user.isVerified = true;
        return user;
    }

    @NotNull
    public static User createStaff(
            String name,
            String email,
            String encodedPassword,
            Long createdByAdminId
    ) {
        User user = new User();
        user.email = email;
        user.password = encodedPassword;
        user.role = Role.STAFF;
        user.accountStatus = AccountStatus.ACTIVE;
        user.isVerified = false;
        user.createdByAdminId = createdByAdminId;
        return user;
    }
}
