package com.harsh.SITIMS.controller;

import com.harsh.SITIMS.dto.AssignOfficerDTO;
import com.harsh.SITIMS.dto.IncidentDTO;
import com.harsh.SITIMS.entity.Incident;
import com.harsh.SITIMS.service.IncidentService;
import com.harsh.SITIMS.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;
    private final JwtService jwtService;

    // CREATE INCIDENT
    @PostMapping("/create")
    public ResponseEntity<Incident> createIncident(@RequestBody IncidentDTO dto,
                                                   @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        Incident incident = incidentService.createIncident(dto, email);
        return ResponseEntity.ok(incident);
    }

    // GET MY INCIDENTS
    @GetMapping("/my")
    public ResponseEntity<List<Incident>> getMyIncidents(@RequestParam String email) {
        return ResponseEntity.ok(incidentService.getMyIncidents(email));
    }

    // GET ALL INCIDENTS
    @GetMapping("/all")
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }

    // GET INCIDENTS BY USER ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Incident>> getIncidentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(incidentService.getIncidentsByUserId(userId));
    }

    // DELETE INCIDENT
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.ok("Incident deleted successfully");
    }

    // ASSIGN OFFICER
    @PostMapping("/assign")
    public ResponseEntity<String> assignOfficer(@RequestBody AssignOfficerDTO dto) {
        String msg = incidentService.assignOfficer(dto);
        return ResponseEntity.ok(msg);
    }

    // ==============================
    // NEW: GET INCIDENT BY ID
    // ==============================
    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        Incident incident = incidentService.getIncidentById(id); // Add this method in service
        if (incident == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(incident);
    }
}
