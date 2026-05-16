package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.dto.AssignOfficerDTO;
import com.harsh.SITIMS.dto.IncidentDTO;
import com.harsh.SITIMS.entity.Incident;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.repository.IncidentRepository;
import com.harsh.SITIMS.repository.UserRepository;
import com.harsh.SITIMS.service.IncidentHistoryService;
import com.harsh.SITIMS.service.IncidentService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;
    private final IncidentHistoryService historyService;

    @PersistenceContext
    private EntityManager entityManager;

    // ---------------- CREATE ----------------
    @Override
    public Incident createIncident(IncidentDTO dto, String email) {

        Incident incident = new Incident();
        incident.setTitle(dto.getTitle());
        incident.setDescription(dto.getDescription());
        incident.setLocation(dto.getLocation());
        incident.setSeverity(dto.getSeverity());
        incident.setStatus("PENDING");
        incident.setReportedBy(email);

        if (dto.getAssignedOfficerId() != null) {
            User officer = userRepository.findById(dto.getAssignedOfficerId()).orElse(null);
            incident.setAssignedOfficer(officer);
        }

        incident.setLinkedTipId(dto.getLinkedTipId());

        Incident saved = incidentRepository.save(incident);

        historyService.saveHistory(
                saved,
                null,
                "PENDING",
                email,
                email,
                "USER",
                "Incident created",
                "CREATED"
        );

        return saved;
    }

    // ---------------- READ ----------------
    @Override
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    @Override
    public List<Incident> getMyIncidents(String email) {
        return incidentRepository.findByReportedBy(email);
    }

    @Override
    public List<Incident> getIncidentsByOfficer(Long officerId) {
        User officer = userRepository.findById(officerId).orElse(null);
        return incidentRepository.findByAssignedOfficer(officer);
    }

    @Override
    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id).orElse(null);
    }

    // ---------------- UPDATE ----------------
    @Override
    public Incident updateIncident(Long id, IncidentDTO dto) {

        Incident existing = incidentRepository.findById(id).orElse(null);
        if (existing == null) return null;

        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getLocation() != null) existing.setLocation(dto.getLocation());
        if (dto.getSeverity() != null) existing.setSeverity(dto.getSeverity());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());

        return incidentRepository.save(existing);
    }

    @Override
    public void updateStatusByOfficer(Long id,
                                      String status,
                                      String remark,
                                      String officerEmail) {

        Incident incident = incidentRepository.findById(id).orElse(null);
        if (incident == null) return;

        String oldStatus = incident.getStatus();

        incident.setStatus(status);
        incident.setOfficerRemark(remark);

        incidentRepository.save(incident);

        User officer = userRepository.findByEmail(officerEmail).orElse(null);

        String name = officer != null ? officer.getFullName() : "Officer";
        String email = officer != null ? officer.getEmail() : "";
        String role = officer != null ? officer.getRole().name() : "OFFICER";

        historyService.saveHistory(
                incident,
                oldStatus,
                status,
                name,
                email,
                role,
                remark,
                "STATUS_CHANGED"
        );
    }

    @Override
    public String assignOfficer(AssignOfficerDTO dto) {

        Incident incident = incidentRepository.findById(dto.getIncidentId()).orElse(null);
        User officer = userRepository.findById(dto.getOfficerId()).orElse(null);

        if (incident == null || officer == null) {
            return "Invalid data";
        }

        incident.setAssignedOfficer(officer);
        incident.setStatus("ASSIGNED");

        incidentRepository.save(incident);

        historyService.saveHistory(
                incident,
                null,
                "ASSIGNED",
                officer.getFullName(),
                officer.getEmail(),
                officer.getRole().name(),
                "Officer Assigned",
                "ASSIGNED"
        );

        return "Officer Assigned Successfully";
    }

    @Override
    public List<Incident> getIncidentsForOfficer(Long officerId) {
        User officer = userRepository.findById(officerId).orElse(null);
        return incidentRepository.findByAssignedOfficer(officer);
    }

    @Override
    public List<Incident> getIncidentsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return List.of();
        return incidentRepository.findByReportedBy(user.getEmail());
    }

    // ---------------- 🔥 FINAL DELETE FIX ----------------
    @Transactional
    @Override
    public void deleteIncident(Long id) {

        // STEP 1: delete child tables FIRST (IMPORTANT)
        entityManager.createNativeQuery(
                "DELETE FROM remark WHERE incident_id = ?"
        ).setParameter(1, id).executeUpdate();

        entityManager.createNativeQuery(
                "DELETE FROM incident_history WHERE incident_id = ?"
        ).setParameter(1, id).executeUpdate();

        // STEP 2: break FK references
        entityManager.createNativeQuery(
                "UPDATE incident SET assigned_officer_id = NULL, created_by_id = NULL WHERE id = ?"
        ).setParameter(1, id).executeUpdate();

        entityManager.flush();
        entityManager.clear();

        // STEP 3: delete main incident
        entityManager.createNativeQuery(
                "DELETE FROM incident WHERE id = ?"
        ).setParameter(1, id).executeUpdate();
    }
}