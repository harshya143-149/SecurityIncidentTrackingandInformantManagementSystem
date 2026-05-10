package com.harsh.SITIMS.controller;

import com.harsh.SITIMS.dto.UserDTO;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:8080") // ✅ FIXED — restricted origin
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    // CREATE ANY USER
    @PostMapping("/users")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO dto) {
        User user = adminUserService.addUser(toEntity(dto));
        return ResponseEntity.ok(toDTO(user));
    }

    // CREATE OFFICER
    @PostMapping("/officers/add")
    public ResponseEntity<UserDTO> addOfficer(@RequestBody UserDTO dto) {
        User officer = adminUserService.addOfficer(toEntity(dto));
        return ResponseEntity.ok(toDTO(officer));
    }

    // GET ALL OFFICERS
    @GetMapping("/officers")
    public ResponseEntity<List<UserDTO>> getAllOfficers() {
        List<UserDTO> officers = adminUserService.getAllOfficers()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(officers);
    }

    // GET OFFICER BY ID
    @GetMapping("/officers/{id}")
    public ResponseEntity<UserDTO> getOfficerById(@PathVariable Long id) {
        User officer = adminUserService.getOfficer(id);
        if (officer == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDTO(officer));
    }

    // UPDATE OFFICER
    @PutMapping("/officers/{id}")
    public ResponseEntity<UserDTO> updateOfficer(@PathVariable Long id,
                                                  @RequestBody UserDTO dto) {
        User updated = adminUserService.updateOfficer(id, toEntity(dto));
        return ResponseEntity.ok(toDTO(updated));
    }

    // DELETE OFFICER
    @DeleteMapping("/officers/{id}")
    public ResponseEntity<String> deleteOfficer(@PathVariable Long id) {
        adminUserService.deleteOfficer(id);
        return ResponseEntity.ok("Officer deleted successfully");
    }

    // GET ALL USERS
    @GetMapping("/users/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = adminUserService.getAllUsers()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // UPDATE USER
    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                               @RequestBody UserDTO dto) {
        User updated = adminUserService.updateUser(id, toEntity(dto));
        return ResponseEntity.ok(toDTO(updated));
    }

    // DELETE USER
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        adminUserService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // ✅ Convert Entity to DTO — never expose password
    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        // password NOT set
        return dto;
    }

    // ✅ Convert DTO to Entity
    private User toEntity(UserDTO dto) {
        User user = new User();
        user.setFullName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        if (dto.getRole() != null) user.setRole(dto.getRole());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
        return user;
    }
}
