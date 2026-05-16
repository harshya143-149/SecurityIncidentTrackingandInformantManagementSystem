package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.dto.RemarkDTO;
import com.harsh.SITIMS.entity.*;
import com.harsh.SITIMS.repository.*;
import com.harsh.SITIMS.service.IncidentHistoryService;
import com.harsh.SITIMS.service.RemarkService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RemarkServiceImpl implements RemarkService {

    private final RemarkRepository remarkRepository;
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;
    private final IncidentHistoryService incidentHistoryService;

    @Override
    @Transactional
    public RemarkDTO addRemark(RemarkDTO dto, String officerEmail) {

        if (dto.getText() == null || dto.getText().trim().isEmpty()) {
            throw new RuntimeException("Remark cannot be empty");
        }

        Incident incident = incidentRepository.findById(dto.getIncidentId())
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        User officer = userRepository.findByEmail(officerEmail)
                .orElseThrow(() -> new RuntimeException("Officer not found"));

        String officerName =
                (officer.getName() != null && !officer.getName().isBlank())
                        ? officer.getName()
                        : officer.getEmail();

        Remark remark = Remark.builder()
                .text(dto.getText().trim())
                .createdAt(LocalDateTime.now())
                .incident(incident)
                .officer(officer)
                .build();

        remarkRepository.save(remark);

        // ✅ FIXED: proper name ALWAYS passed
        incidentHistoryService.saveHistory(
                incident,
                incident.getStatus(),
                incident.getStatus(),
                officerName,
                officerEmail,
                "OFFICER",
                dto.getText(),
                "REMARKED"
        );

        dto.setId(remark.getId());
        dto.setOfficerId(officer.getId());
        dto.setOfficerName(officerName);
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

                    User officer = r.getOfficer();

                    String name =
                            (officer != null && officer.getName() != null && !officer.getName().isBlank())
                                    ? officer.getName()
                                    : "System";

                    RemarkDTO dto = new RemarkDTO();
                    dto.setId(r.getId());
                    dto.setIncidentId(incidentId);
                    dto.setText(r.getText());
                    dto.setCreatedAt(r.getCreatedAt());
                    dto.setOfficerId(officer != null ? officer.getId() : null);
                    dto.setOfficerName(name);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<RemarkDTO> getRemarksByIncidentId(Long incidentId) {
        return getRemarksForIncident(incidentId);
    }

    @Override
    @Transactional
    public RemarkDTO updateRemark(Long remarkId, String text, String officerEmail) {

        Remark remark = remarkRepository.findById(remarkId)
                .orElseThrow(() -> new RuntimeException("Remark not found"));

        if (!remark.getOfficer().getEmail().equals(officerEmail)) {
            throw new RuntimeException("Not allowed to edit this remark");
        }

        remark.setText(text.trim());
        remarkRepository.save(remark);

        User officer = remark.getOfficer();

        String name =
                (officer.getName() != null && !officer.getName().isBlank())
                        ? officer.getName()
                        : officer.getEmail();

        RemarkDTO dto = new RemarkDTO();
        dto.setId(remark.getId());
        dto.setIncidentId(remark.getIncident().getId());
        dto.setText(remark.getText());
        dto.setCreatedAt(remark.getCreatedAt());
        dto.setOfficerId(officer.getId());
        dto.setOfficerName(name);

        return dto;
    }

    @Override
    @Transactional
    public void deleteRemark(Long remarkId, String officerEmail) {

        Remark remark = remarkRepository.findById(remarkId)
                .orElseThrow(() -> new RuntimeException("Remark not found"));

        if (!remark.getOfficer().getEmail().equals(officerEmail)) {
            throw new RuntimeException("Not allowed to delete this remark");
        }

        remarkRepository.delete(remark);
    }
}