package com.harsh.SITIMS.dto;

import lombok.Data;

@Data
public class InformantDTO {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;

    private String password;
}
