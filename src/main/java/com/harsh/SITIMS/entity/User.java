package com.harsh.SITIMS.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;
    private String phone;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    // ---------- FIXED METHODS ----------
    public String getName() {
        return this.fullName;   // return fullName
    }

    public void setName(String name) {
        this.fullName = name;   // assign to fullName
    }
}
