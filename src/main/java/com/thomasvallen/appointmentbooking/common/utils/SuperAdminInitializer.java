package com.thomasvallen.appointmentbooking.common.utils;

import com.thomasvallen.appointmentbooking.entity.User;
import com.thomasvallen.appointmentbooking.entity.UserProfile;
import com.thomasvallen.appointmentbooking.enums.AccountStatus;
import com.thomasvallen.appointmentbooking.enums.Role;
import com.thomasvallen.appointmentbooking.repository.UserProfileRepository;
import com.thomasvallen.appointmentbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuperAdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${app.super-admin.email}")
    private String email;

    @Value("${app.super-admin.password}")
    private String password;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            if (!userRepository.existsByRole(Role.SUPER_ADMIN)) {
                log.info("Initializing SuperAdmin user...");

                User superAdmin = User.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .role(Role.SUPER_ADMIN)
                        .accountStatus(AccountStatus.ACTIVE)
                        .isVerified(true)
                        .build();

                User savedUser = userRepository.save(superAdmin);
                log.debug("SuperAdmin user created with ID: {}", savedUser.getId());

                UserProfile superAdminProfile = UserProfile.builder()
                        .user(savedUser)
                        .name("SuperAdmin")
                        .phoneNumber("+918888888888")
                        .department("Board of Directors")
                        .designation("Super Administrator")
                        .isProfileComplete(true)
                        .build();

                userProfileRepository.save(superAdminProfile);
                log.info("SuperAdmin initialized successfully with email: {}", email);
            } else {
                log.debug("SuperAdmin user already exists. Skipping initialization.");
            }
        } catch (Exception e) {
            log.error("Error initializing SuperAdmin user", e);
            throw e;
        }
    }


}
