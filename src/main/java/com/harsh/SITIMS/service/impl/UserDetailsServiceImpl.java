package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.entity.Informant;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.repository.InformantRepository;
import com.harsh.SITIMS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final InformantRepository informantRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // ================= USERS =================
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            return new UserDetailsImpl(user);
        }

        // ================= INFORMANTS =================
        Informant informant = informantRepository.findByEmail(email).orElse(null);

        if (informant != null) {

            return new UserDetailsImplForInformant(informant);
        }

        throw new UsernameNotFoundException("User not found: " + email);
    }
}