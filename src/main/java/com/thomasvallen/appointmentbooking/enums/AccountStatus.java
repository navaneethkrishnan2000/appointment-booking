package com.thomasvallen.appointmentbooking.enums;

public enum AccountStatus {

    ACTIVE("Account is active and accessible"),
    INACTIVE("Account is inactive but not deactivated"),
    SUSPENDED("Account is suspended by admin action"),
    DEACTIVATED("Account is deactivated and cannot be reactivated");

    private final String description;

    AccountStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAccessible() {
        return this == ACTIVE;
    }
}
