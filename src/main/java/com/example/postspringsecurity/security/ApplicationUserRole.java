package com.example.postspringsecurity.security;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.example.postspringsecurity.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    VIEWER(Sets.newHashSet(
            VIEWER_READ,
            VIEWER_COMMENT)),
    AUTHOR(Sets.newHashSet(
            AUTHOR_READ,
            AUTHOR_WRITE)),
    ADMIN(Sets.newHashSet(
            VIEWER_READ,
            VIEWER_COMMENT,
            AUTHOR_READ,
            AUTHOR_WRITE,
            ADMIN_REMOVE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }
}
