package com.harsh.SITIMS.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "informants")
@Data
@NoArgsConstructor
public class Informant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // added username field
    private String name;
    private String phone;
    private String email;
    private String address;
    private String password;

    // Full name getter for compatibility
    public String getFullName() {
        return this.name;
    }

    // Proper setters
    public void setFullName(String fullName) {
        this.name = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
