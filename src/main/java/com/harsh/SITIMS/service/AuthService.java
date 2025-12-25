package com.harsh.SITIMS.service;

import com.harsh.SITIMS.dto.RegisterDTO;
import com.harsh.SITIMS.entity.Informant;
import com.harsh.SITIMS.entity.User;

public interface AuthService {

    // Existing authentication methods
    User authenticate(String email, String password);
    Informant authenticateInformant(String email, String password);

    // New registration methods
    User registerUser(RegisterDTO dto);
    Informant registerInformant(RegisterDTO dto);
}
