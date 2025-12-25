package com.harsh.SITIMS.controller;

import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    // CREATE ANY USER
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        return adminUserService.addUser(user);
    }

    // CREATE OFFICER (optional)
    @PostMapping("/officers/add")
    public User addOfficer(@RequestBody User officer) {
        return adminUserService.addOfficer(officer);
    }

    // GET ALL OFFICERS
    @GetMapping("/officers")
    public List<User> getAllOfficers() {
        return adminUserService.getAllOfficers();
    }

    // GET OFFICER BY ID
    @GetMapping("/officers/{id}")
    public User getOfficerById(@PathVariable Long id) {
        return adminUserService.getOfficer(id);
    }

    // UPDATE OFFICER
    @PutMapping("/officers/{id}")
    public User updateOfficer(@PathVariable Long id, @RequestBody User updatedOfficer) {
        return adminUserService.updateOfficer(id, updatedOfficer);
    }

    // DELETE OFFICER
    @DeleteMapping("/officers/{id}")
    public String deleteOfficer(@PathVariable Long id) {
        adminUserService.deleteOfficer(id);
        return "Officer deleted successfully";
    }

    // GET ALL USERS
    @GetMapping("/users/all")
    public List<User> getAllUsers() {
        return adminUserService.getAllUsers();
    }

    // GENERIC UPDATE USER
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return adminUserService.updateUser(id, updatedUser);
    }

    // GENERIC DELETE USER
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        adminUserService.deleteUserById(id);
        return "User deleted successfully";
    }
}
