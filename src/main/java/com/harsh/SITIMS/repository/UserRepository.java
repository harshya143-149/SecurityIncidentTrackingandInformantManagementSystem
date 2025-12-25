package com.harsh.SITIMS.repository;

import com.harsh.SITIMS.entity.Role;
import com.harsh.SITIMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // Get all officers OR all admins
    List<User> findByRole(Role role);

    // Used for login validation
    Optional<User> findByEmailAndRole(String email, Role role);

    // FIXED -----
    Optional<User> findByUsername(String username);
}
