package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.dto.IncidentHistoryDTO;
import com.harsh.SITIMS.entity.Incident;
import com.harsh.SITIMS.entity.IncidentHistory;
import com.harsh.SITIMS.repository.IncidentHistoryRepository;
import com.harsh.SITIMS.service.IncidentHistoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentHistoryServiceImpl implements IncidentHistoryService {

    private final IncidentHistoryRepository historyRepository;

    @Override
    public void saveHistory(
            Incident incident,
            String oldStatus,
            String newStatus,
            String changedByName,
            String changedByEmail,
            String changedByRole,
            String remark,
            String actionType
    ) {

        // 🔥 FINAL CLEAN FIX
        changedByName = cleanName(changedByName);
        changedByEmail = cleanEmail(changedByEmail);
        changedByRole = cleanRole(changedByRole);
        remark = (remark == null || remark.isBlank()) ? "-" : remark;
        actionType = (actionType == null || actionType.isBlank()) ? "UPDATED" : actionType;

        IncidentHistory history = IncidentHistory.builder()
                .incident(incident)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedByName(changedByName)
                .changedByEmail(changedByEmail)
                .changedByRole(changedByRole)
                .remark(remark)
                .actionType(actionType)
                .build();

        historyRepository.save(history);
    }

    // =========================
    // CLEAN NAME (IMPORTANT FIX)
    // =========================
    private String cleanName(String name) {

        if (name == null || name.isBlank()) {
            return "System";
        }

        // remove bad system values
        if (name.toLowerCase().contains("system")) {
            return "System";
        }

        // remove email-like values
        if (name.contains("@")) {
            return name.split("@")[0];   // 🔥 FIX officer2system issue
        }

        return name;
    }

    private String cleanEmail(String email) {
        if (email == null || email.isBlank()) {
            return "SYSTEM";
        }
        return email;
    }

    private String cleanRole(String role) {
        if (role == null || role.isBlank()) {
            return "SYSTEM";
        }
        return role;
    }

    @Override
    public List<IncidentHistoryDTO> getHistoryByIncidentId(Long incidentId) {

        return historyRepository.findByIncidentIdOrderByChangedAtDesc(incidentId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteHistory(Long id) {
        historyRepository.deleteById(id);
    }

    private IncidentHistoryDTO toDTO(IncidentHistory h) {

        IncidentHistoryDTO dto = new IncidentHistoryDTO();

        dto.setId(h.getId());
        dto.setIncidentId(h.getIncident() != null ? h.getIncident().getId() : null);

        dto.setChangedByName(cleanName(h.getChangedByName()));
        dto.setChangedByEmail(h.getChangedByEmail());
        dto.setChangedByRole(h.getChangedByRole());

        dto.setOldStatus(h.getOldStatus());
        dto.setNewStatus(h.getNewStatus());

        dto.setRemark(h.getRemark());
        dto.setChangedAt(h.getChangedAt());
        dto.setActionType(h.getActionType());

        return dto;
    }
}