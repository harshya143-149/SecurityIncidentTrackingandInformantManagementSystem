package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.dto.RemarkDTO;
import com.harsh.SITIMS.entity.*;
import com.harsh.SITIMS.repository.*;
import com.harsh.SITIMS.service.RemarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RemarkServiceImpl implements RemarkService {

    private final RemarkRepository remarkRepository;
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    @Override
    public RemarkDTO addRemark(RemarkDTO dto, String officerEmail) {

        Incident incident = incidentRepository.findById(dto.getIncidentId())
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        User officer = userRepository.findByEmail(officerEmail)
                .orElseThrow(() -> new RuntimeException("Officer not found"));

        Remark remark = Remark.builder()
                .text(dto.getText())
                .createdAt(LocalDateTime.now())
                .incident(incident)
                .officer(officer)
                .build();

        remarkRepository.save(remark);

        dto.setId(remark.getId());
        dto.setOfficerId(officer.getId());
        dto.setOfficerName(officer.getFullName());
        dto.setCreatedAt(remark.getCreatedAt());
        return dto;
    }

    @Override
    public List<RemarkDTO> getRemarksForIncident(Long incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        return remarkRepository.findByIncidentOrderByCreatedAtDesc(incident)
                .stream()
                .map(r -> {
                    RemarkDTO dto = new RemarkDTO();
                    dto.setId(r.getId());
                    dto.setIncidentId(incidentId);
                    dto.setText(r.getText());
                    dto.setCreatedAt(r.getCreatedAt());
                    dto.setOfficerId(r.getOfficer().getId());
                    dto.setOfficerName(r.getOfficer().getFullName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ✅ FIXED — was returning empty list before
    @Override
    public List<RemarkDTO> getRemarksByIncidentId(Long incidentId) {
        return getRemarksForIncident(incidentId);
    }
}
