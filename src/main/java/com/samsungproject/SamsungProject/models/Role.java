package com.samsungproject.SamsungProject.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    WORKER, DIRECTOR;

    @Override
    public String getAuthority() {
        return name();
    }
}