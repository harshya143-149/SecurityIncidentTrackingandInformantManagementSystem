package com.harsh.SITIMS.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Officer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String phone;

    private String rank;  // Inspector, Sub-Inspector, Head Constable, etc.

    private String password; // encrypted same as user

    private String role = "OFFICER";
}
