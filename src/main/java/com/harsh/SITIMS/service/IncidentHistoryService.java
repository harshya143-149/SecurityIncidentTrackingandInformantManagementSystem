package com.harsh.SITIMS.service;

import com.harsh.SITIMS.dto.IncidentHistoryDTO;
import com.harsh.SITIMS.entity.Incident;

import java.util.List;

public interface IncidentHistoryService {

    // Save a history record
    void saveHistory(
            Incident incident,
            String oldStatus,
            String newStatus,
            String changedByName,
            String changedByEmail,
            String changedByRole,
            String remark,
            String actionType
    );

    // Get all history for an incident
    List<IncidentHistoryDTO> getHistoryByIncidentId(Long incidentId);

    void deleteHistory(Long id);
}