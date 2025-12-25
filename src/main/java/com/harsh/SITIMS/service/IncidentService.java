package com.harsh.SITIMS.service;

import com.harsh.SITIMS.dto.AssignOfficerDTO;
import com.harsh.SITIMS.dto.IncidentDTO;
import com.harsh.SITIMS.entity.Incident;

import java.util.List;

public interface IncidentService {

    // CREATE INCIDENT
    Incident createIncident(IncidentDTO dto, String username);

    // GET ALL INCIDENTS
    List<Incident> getAllIncidents();

    // GET INCIDENTS CREATED BY A USER
    List<Incident> getMyIncidents(String username);

    // GET INCIDENTS ASSIGNED TO AN OFFICER
    List<Incident> getIncidentsByOfficer(Long officerId);

    // GET INCIDENT BY ID
    Incident getIncidentById(Long id);

    // UPDATE INCIDENT
    Incident updateIncident(Long id, Incident updated);

    // UPDATE STATUS BY OFFICER
    void updateStatusByOfficer(Long id, String status, String remark);

    // ASSIGN OFFICER TO INCIDENT
    String assignOfficer(AssignOfficerDTO dto);

    // GET INCIDENTS FOR OFFICER (alternative)
    List<Incident> getIncidentsForOfficer(Long officerId);

    // GET INCIDENTS BY USER ID
    List<Incident> getIncidentsByUserId(Long userId);

    // DELETE INCIDENT
    void deleteIncident(Long id);
}
