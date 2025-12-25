package com.harsh.SITIMS.service;

import com.harsh.SITIMS.entity.User;
import java.util.List;

public interface AdminUserService {

    // Existing officer methods
    User addOfficer(User officer);
    List<User> getAllOfficers();
    User getOfficer(Long id);
    User updateOfficer(Long id, User officer);
    void deleteOfficer(Long id);

    // All users
    User addUser(User user);             // Add any user with dynamic role
    List<User> getAllUsers();            // Get all users

    // Generic update and delete for any user
    User updateUser(Long id, User user);
    void deleteUserById(Long id);
}
