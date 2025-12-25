package com.harsh.SITIMS.service;

import com.harsh.SITIMS.dto.InformantDTO;
import com.harsh.SITIMS.entity.Informant;

import java.util.List;

public interface InformantService {

    // CRUD methods
    InformantDTO save(InformantDTO dto);
    List<InformantDTO> getAll();
    InformantDTO getById(Long id);
    InformantDTO update(Long id, InformantDTO dto);
    void delete(Long id);

    // Additional methods for admin
    InformantDTO addInformant(InformantDTO dto);
    List<InformantDTO> getAllInformants();

    InformantDTO getInformantById(Long id);

    InformantDTO updateInformant(Long id, InformantDTO dto);

    void deleteInformant(Long id);

    Informant authenticate(String email, String password);
}
