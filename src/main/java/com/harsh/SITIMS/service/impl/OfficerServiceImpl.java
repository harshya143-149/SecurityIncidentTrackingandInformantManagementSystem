package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.entity.Role;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.repository.UserRepository;
import com.harsh.SITIMS.service.OfficerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficerServiceImpl implements OfficerService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createOfficer(User officer) {

        officer.setPassword(
                passwordEncoder.encode(officer.getPassword())
        );

        officer.setRole(Role.OFFICER);

        return userRepository.save(officer);
    }

    @Override
    public User updateOfficer(Long id, User updatedOfficer) {

        User officer = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Officer not found"));

        // ✅ FIXED
        officer.setName(updatedOfficer.getName());

        officer.setEmail(updatedOfficer.getEmail());

        officer.setPhone(updatedOfficer.getPhone());

        // ✅ ALWAYS KEEP ROLE AS OFFICER
        officer.setRole(Role.OFFICER);

        // ✅ PASSWORD UPDATE
        if (updatedOfficer.getPassword() != null &&
                !updatedOfficer.getPassword().isEmpty()) {

            officer.setPassword(
                    passwordEncoder.encode(
                            updatedOfficer.getPassword()
                    )
            );
        }

        return userRepository.save(officer);
    }

    @Override
    public void deleteOfficer(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllOfficers() {
        return userRepository.findByRole(Role.OFFICER);
    }

    @Override
    public User getOfficerById(Long id) {

        User officer = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Officer not found"));

        if (officer.getRole() != Role.OFFICER) {
            throw new RuntimeException("User is not an officer");
        }

        return officer;
    }

    @Override
    public User getOfficerByEmail(String email) {

        User officer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Officer not found"));

        if (officer.getRole() != Role.OFFICER) {
            throw new RuntimeException("User is not an officer");
        }

        return officer;
    }
}