package com.thomasvallen.appointmentbooking.config.security;

import com.thomasvallen.appointmentbooking.entity.User;
import com.thomasvallen.appointmentbooking.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private final Long userId;
    private final String email;
    private final String password;
    private final Role role;
    private final boolean isActive;
    private final boolean isVerified;

    public UserPrincipal(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.isActive = user.isActive();
        this.isVerified = user.getIsVerified();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public String getPassword() { return password; }

    @Override public String getUsername() { return email; }

    @Override public boolean isAccountNonExpired() { return true; }

    @Override public boolean isAccountNonLocked() { return true; }

    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override public boolean isEnabled() { return isActive; }

    public Long getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    public boolean isVerified() { return isVerified; }
}
