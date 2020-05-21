package com.example.postspringsecurity.security;


public enum ApplicationUserPermission {
    VIEWER_READ("viewer:read"),
    VIEWER_COMMENT("viewer:comment"),
    AUTHOR_READ("author:read"),
    AUTHOR_WRITE("author:write"),
    ADMIN_REMOVE("admin:remove");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
