package com.example.ecommerce.domain.user;

import java.util.Set;
import java.util.Objects;

public class User {

    private final UserId id;
    private final Set<UserRole> roles;
    private boolean active;

    public User(UserId id, Set<UserRole> roles) {
        this.id = Objects.requireNonNull(id);
        this.roles = Set.copyOf(roles);
        this.active = true;
    }

    public UserId getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public boolean hasRole(UserRole role) {
        return roles.contains(role);
    }

    public void deactivate() {
        this.active = false;
    }
}
