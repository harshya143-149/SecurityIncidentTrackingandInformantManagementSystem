package com.harsh.SITIMS.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;     // Use email instead of username
    private String password;
}
