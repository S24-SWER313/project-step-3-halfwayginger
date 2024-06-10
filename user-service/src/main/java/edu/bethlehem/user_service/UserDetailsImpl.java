package edu.bethlehem.user_service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsImpl extends UserDetails {
    public Long id = 0L;

    public String getUsername();

    public Long getId();
}
