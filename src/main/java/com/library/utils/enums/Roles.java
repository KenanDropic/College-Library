package com.library.utils.enums;

public enum Roles {
    ADMIN("admin"),
    LIBRARIAN("librarian"),
    USER("user");

    private final String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
