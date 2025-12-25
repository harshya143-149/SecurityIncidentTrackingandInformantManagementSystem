package com.harsh.SITIMS.service;

import com.harsh.SITIMS.dto.UserDTO;
import com.harsh.SITIMS.entity.User;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO dto, String password);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    void deleteUser(Long id);

    User getUserByEmailAndRole(String email, String role);

    // UPDATED: include email from JWT
    UserDTO updateUser(UserDTO dto, String emailFromToken);

    // NEW METHOD to extract email from JWT
    String getEmailFromToken(String token);
}
