package com.harsh.SITIMS.dto;

import com.harsh.SITIMS.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // ignore nulls during deserialization
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role; // can be null when updating profile
    private String password;

    public UserDTO() {}

    // Full details constructor
    public UserDTO(Long id, String name, String email, String phone, String roleName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        if (roleName != null) this.role = Role.valueOf(roleName.toUpperCase());
    }

    // List display constructor
    public UserDTO(Long id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Simple creation
    public UserDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
