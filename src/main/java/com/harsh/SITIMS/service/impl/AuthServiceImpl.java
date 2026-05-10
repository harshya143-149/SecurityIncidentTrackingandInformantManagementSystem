package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.dto.RegisterDTO;
import com.harsh.SITIMS.entity.Informant;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.entity.Role;
import com.harsh.SITIMS.repository.InformantRepository;
import com.harsh.SITIMS.repository.UserRepository;
import com.harsh.SITIMS.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final InformantRepository informantRepo;
    private final PasswordEncoder passwordEncoder;

    // ==================== AUTHENTICATE USER ====================
    @Override
    public User authenticate(String email, String password) {
        User u = userRepo.findByEmail(email).orElse(null);
        if (u == null) return null;

        // Admin / Officer / User use BCrypt
        if (passwordEncoder.matches(password, u.getPassword())) return u;
        return null;
    }

    // ==================== AUTHENTICATE INFORMANT ====================
    @Override
    public Informant authenticateInformant(String email, String password) {
        Informant i = informantRepo.findByEmail(email).orElse(null);
        if (i == null) return null;

        // ✅ FIXED — BCrypt comparison instead of plain text
        if (passwordEncoder.matches(password, i.getPassword())) return i;
        return null;
    }

    // ==================== REGISTER USER ====================
    @Override
    public User registerUser(RegisterDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        // ✅ BCrypt encoding
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        return userRepo.save(user);
    }

    // ==================== REGISTER INFORMANT ====================
    @Override
    public Informant registerInformant(RegisterDTO dto) {
        Informant informant = new Informant();
        informant.setUsername(dto.getUsername());
        informant.setFullName(dto.getFullName());
        informant.setEmail(dto.getEmail());
        informant.setPhone(dto.getPhone());
        // ✅ FIXED — BCrypt encoding instead of plain text
        informant.setPassword(passwordEncoder.encode(dto.getPassword()));
        return informantRepo.save(informant);
    }
}
