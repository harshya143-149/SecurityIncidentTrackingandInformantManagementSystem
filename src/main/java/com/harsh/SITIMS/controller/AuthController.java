package com.harsh.SITIMS.controller;

import com.harsh.SITIMS.dto.AuthRequest;
import com.harsh.SITIMS.dto.RegisterDTO;
import com.harsh.SITIMS.entity.Informant;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.service.AuthService;
import com.harsh.SITIMS.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthRequest dto) {

        // === Authenticate User/Admin first (no change here) ===
        User user = authService.authenticate(dto.getEmail(), dto.getPassword());
        if (user != null) {
            String token = jwtService.generateToken(user.getEmail());
            Map<String, Object> res = new HashMap<>();
            res.put("token", token);
            res.put("role", user.getRole().name());
            res.put("id", user.getId());
            res.put("email", user.getEmail());
            res.put("fullName", user.getFullName());
            return ResponseEntity.ok(res);
        }

        // === Only check Informants if User/Admin not found ===
        Informant informant = authService.authenticateInformant(dto.getEmail(), dto.getPassword());
        if (informant != null) {
            String token = jwtService.generateToken(informant.getEmail());
            Map<String, Object> res = new HashMap<>();
            res.put("token", token);
            res.put("role", "INFORMANT");
            res.put("id", informant.getId());
            res.put("email", informant.getEmail());
            res.put("fullName", informant.getFullName());
            return ResponseEntity.ok(res);
        }

        // Invalid credentials
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }

    // ==================== REGISTER ENDPOINT ====================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
        try {
            if (dto.getRole() == null || dto.getRole().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Role is required"));
            }

            if (dto.getRole().equalsIgnoreCase("USER")) {
                User user = authService.registerUser(dto);
                return ResponseEntity.ok(Map.of("message", "User registered successfully", "id", user.getId()));
            } else if (dto.getRole().equalsIgnoreCase("INFORMANT")) {
                Informant informant = authService.registerInformant(dto);
                return ResponseEntity.ok(Map.of("message", "Informant registered successfully", "id", informant.getId()));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid role"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal Server Error: " + e.getMessage()));
        }
    }
}
