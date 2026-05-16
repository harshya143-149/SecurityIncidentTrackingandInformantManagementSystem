package com.harsh.SITIMS.service;

import com.harsh.SITIMS.dto.AssignOfficerDTO;
import com.harsh.SITIMS.dto.IncidentDTO;
import com.harsh.SITIMS.entity.Incident;

import java.util.List;

public interface IncidentService {

    Incident createIncident(IncidentDTO dto, String username);

    List<Incident> getAllIncidents();

    List<Incident> getMyIncidents(String username);

    List<Incident> getIncidentsByOfficer(Long officerId);

    Incident getIncidentById(Long id);

    Incident updateIncident(Long id, IncidentDTO dto);

    // ✅ UPDATED
    void updateStatusByOfficer(
            Long id,
            String status,
            String remark,
            String officerEmail
    );

    String assignOfficer(AssignOfficerDTO dto);

    List<Incident> getIncidentsForOfficer(Long officerId);

    List<Incident> getIncidentsByUserId(Long userId);

    void deleteIncident(Long id);
}