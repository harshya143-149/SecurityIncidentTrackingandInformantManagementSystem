package com.harsh.SITIMS.dto;

public class AuthResponse {

    private String token;
    private String message;
    private String role;   // optional (for frontend)

    public AuthResponse() {}

    // Constructor used in AuthServiceImpl (token + message)
    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    // Optional constructor if you want to send role also
    public AuthResponse(String token, String message, String role) {
        this.token = token;
        this.message = message;
        this.role = role;
    }

    // Getters & setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
