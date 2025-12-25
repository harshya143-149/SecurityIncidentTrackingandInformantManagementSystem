package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.dto.AssignOfficerDTO;
import com.harsh.SITIMS.dto.IncidentDTO;
import com.harsh.SITIMS.entity.Incident;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.repository.IncidentRepository;
import com.harsh.SITIMS.repository.UserRepository;
import com.harsh.SITIMS.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

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

        return incidentRepository.save(incident);
    }

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

    @Override
    public Incident updateIncident(Long id, Incident updatedIncident) {
        Incident existing = incidentRepository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setTitle(updatedIncident.getTitle());
        existing.setDescription(updatedIncident.getDescription());
        existing.setLocation(updatedIncident.getLocation());
        existing.setSeverity(updatedIncident.getSeverity());

        return incidentRepository.save(existing);
    }

    @Override
    public void updateStatusByOfficer(Long id, String status, String remark) {
        Incident incident = incidentRepository.findById(id).orElse(null);
        if (incident == null) return;

        incident.setStatus(status);
        incident.setOfficerRemark(remark);

        incidentRepository.save(incident);
    }

    @Override
    public String assignOfficer(AssignOfficerDTO dto) {
        Incident incident = incidentRepository.findById(dto.getIncidentId()).orElse(null);
        User officer = userRepository.findById(dto.getOfficerId()).orElse(null);

        if (incident == null || officer == null) return "Invalid data";

        incident.setAssignedOfficer(officer);
        incident.setStatus("ASSIGNED");

        incidentRepository.save(incident);

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

    @Override
    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }
}
