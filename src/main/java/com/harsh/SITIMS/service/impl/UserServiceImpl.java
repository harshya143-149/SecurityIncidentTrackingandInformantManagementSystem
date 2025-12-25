package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.dto.UserDTO;
import com.harsh.SITIMS.entity.Role;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.repository.UserRepository;
import com.harsh.SITIMS.service.UserService;
import com.harsh.SITIMS.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserDTO createUser(UserDTO dto, String rawPassword) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(Role.valueOf(String.valueOf(dto.getRole())));
        user.setPassword(passwordEncoder.encode(rawPassword));

        userRepository.save(user);
        dto.setId(user.getId());
        return dto;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserDTO(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getPhone(),
                        u.getRole().name()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getPhone(), u.getRole().name());
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByEmailAndRole(String email, String roleStr) {
        Role role;
        try {
            role = Role.valueOf(roleStr.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid role: " + roleStr);
        }

        return userRepository.findByEmailAndRole(email, role)
                .orElseThrow(() -> new RuntimeException("User with role " + role + " not found"));
    }

    // ======================
    // UPDATE USER PROFILE
    // ======================
    @Override
    public UserDTO updateUser(UserDTO dto, String emailFromToken) {
        User user = userRepository.findByEmail(emailFromToken)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getName() != null && !dto.getName().isEmpty()) user.setName(dto.getName());
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) user.setPhone(dto.getPhone());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(user);
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole().name());
    }

    @Override
    public String getEmailFromToken(String token) {
        return jwtService.extractUsername(token);
    }
}
