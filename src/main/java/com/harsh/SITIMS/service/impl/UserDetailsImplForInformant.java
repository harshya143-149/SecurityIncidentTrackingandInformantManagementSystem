package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.entity.Informant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImplForInformant implements UserDetails {

    private final Informant informant;

    public UserDetailsImplForInformant(Informant informant) {
        this.informant = informant;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_INFORMANT"));
    }

    @Override
    public String getPassword() {
        return informant.getPassword();
    }

    @Override
    public String getUsername() {
        return informant.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}