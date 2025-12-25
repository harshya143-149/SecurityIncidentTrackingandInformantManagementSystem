package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.dto.InformantDTO;
import com.harsh.SITIMS.entity.Informant;
import com.harsh.SITIMS.repository.InformantRepository;
import com.harsh.SITIMS.service.InformantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InformantServiceImpl implements InformantService {

    private final InformantRepository repository;

    // ================= ADD INFORMANT =================
    @Override
    public InformantDTO addInformant(InformantDTO dto) {
        Informant informant = new Informant();
        informant.setName(dto.getName());
        informant.setPhone(dto.getPhone());
        informant.setEmail(dto.getEmail());
        informant.setAddress(dto.getAddress());
        informant.setPassword(dto.getPassword()); // store plain password

        Informant saved = repository.save(informant);

        dto.setId(saved.getId());
        dto.setName(saved.getName());
        dto.setPhone(saved.getPhone());
        dto.setEmail(saved.getEmail());
        dto.setAddress(saved.getAddress());
        dto.setPassword(saved.getPassword());

        return dto;
    }

    // ================= GET ALL =================
    @Override
    public List<InformantDTO> getAllInformants() {
        return repository.findAll().stream().map(i -> {
            InformantDTO dto = new InformantDTO();
            dto.setId(i.getId());
            dto.setName(i.getName());
            dto.setPhone(i.getPhone());
            dto.setEmail(i.getEmail());
            dto.setAddress(i.getAddress());
            dto.setPassword(i.getPassword());
            return dto;
        }).collect(Collectors.toList());
    }

    // ================= GET BY ID =================
    @Override
    public InformantDTO getInformantById(Long id) {
        Informant i = repository.findById(id).orElse(null);
        if (i == null) return null;

        InformantDTO dto = new InformantDTO();
        dto.setId(i.getId());
        dto.setName(i.getName());
        dto.setPhone(i.getPhone());
        dto.setEmail(i.getEmail());
        dto.setAddress(i.getAddress());
        dto.setPassword(i.getPassword());

        return dto;
    }

    // ================= UPDATE =================
    @Override
    public InformantDTO updateInformant(Long id, InformantDTO dto) {
        Informant i = repository.findById(id).orElse(null);
        if (i == null) return null;

        i.setName(dto.getName());
        i.setPhone(dto.getPhone());
        i.setEmail(dto.getEmail());
        i.setAddress(dto.getAddress());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            i.setPassword(dto.getPassword()); // update plain password
        }

        Informant saved = repository.save(i);

        dto.setName(saved.getName());
        dto.setPhone(saved.getPhone());
        dto.setEmail(saved.getEmail());
        dto.setAddress(saved.getAddress());
        dto.setPassword(saved.getPassword());

        return dto;
    }

    // ================= DELETE =================
    @Override
    public void deleteInformant(Long id) {
        repository.deleteById(id);
    }

    // ================= AUTHENTICATE =================
    @Override
    public Informant authenticate(String email, String password) {
        Informant i = repository.findByEmail(email).orElse(null);
        if (i == null) return null;

        return i.getPassword().equals(password) ? i : null; // plain text compare
    }

    // Unused old methods
    @Override public InformantDTO save(InformantDTO dto) { return null; }
    @Override public List<InformantDTO> getAll() { return List.of(); }
    @Override public InformantDTO getById(Long id) { return null; }
    @Override public InformantDTO update(Long id, InformantDTO dto) { return null; }
    @Override public void delete(Long id) {}
}
