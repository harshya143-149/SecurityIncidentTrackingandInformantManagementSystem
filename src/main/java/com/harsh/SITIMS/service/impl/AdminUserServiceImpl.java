package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.entity.Role;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.repository.UserRepository;
import com.harsh.SITIMS.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Add user with dynamic role
    @Override
    public User addUser(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.USER); // default role
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Officer-specific methods
    @Override
    public User addOfficer(User officer) {
        officer.setRole(Role.OFFICER);
        officer.setPassword(passwordEncoder.encode(officer.getPassword()));
        return userRepository.save(officer);
    }

    @Override
    public List<User> getAllOfficers() {
        return userRepository.findByRole(Role.OFFICER);
    }

    @Override
    public User getOfficer(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateOfficer(Long id, User updated) {
        User officer = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Officer not found"));

        officer.setFullName(updated.getFullName());
        officer.setEmail(updated.getEmail());
        officer.setPhone(updated.getPhone());
        officer.setRole(Role.OFFICER); // must remain officer
        if (updated.getPassword() != null && !updated.getPassword().isEmpty()) {
            officer.setPassword(passwordEncoder.encode(updated.getPassword()));
        }

        return userRepository.save(officer);
    }

    @Override
    public void deleteOfficer(Long id) {
        userRepository.deleteById(id);
    }

    // Generic user methods
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, User updated) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(updated.getFullName());
        user.setEmail(updated.getEmail());
        user.setPhone(updated.getPhone());
        user.setRole(updated.getRole());
        if (updated.getPassword() != null && !updated.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updated.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
