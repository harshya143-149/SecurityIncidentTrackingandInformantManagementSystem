        package com.harsh.SITIMS.controller;

import com.harsh.SITIMS.dto.AssignOfficerDTO;
import com.harsh.SITIMS.dto.IncidentDTO;
import com.harsh.SITIMS.entity.Incident;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.repository.UserRepository;
import com.harsh.SITIMS.service.IncidentHistoryService;
import com.harsh.SITIMS.service.IncidentService;
import com.harsh.SITIMS.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final IncidentHistoryService historyService;

    // CREATE INCIDENT
    @PostMapping("/create")
    public ResponseEntity<Incident> createIncident(
            @RequestBody IncidentDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        return ResponseEntity.ok(
                incidentService.createIncident(dto, email)
        );
    }

    // GET MY INCIDENTS
    @GetMapping("/my")
    public ResponseEntity<List<Incident>> getMyIncidents(
            @RequestParam String email) {

        return ResponseEntity.ok(
                incidentService.getMyIncidents(email)
        );
    }

    // GET ALL INCIDENTS
    @GetMapping("/all")
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return ResponseEntity.ok(
                incidentService.getAllIncidents()
        );
    }

    // GET BY USER ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Incident>> getIncidentsByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                incidentService.getIncidentsByUserId(userId)
        );
    }

    // DELETE INCIDENT
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteIncident(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        Incident incident = incidentService.getIncidentById(id);

        if (incident == null) {
            return ResponseEntity.notFound().build();
        }

        boolean isOwner =
                incident.getReportedBy() != null &&
                        incident.getReportedBy().equals(email);

        if (!isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You cannot delete this incident");
        }

        incidentService.deleteIncident(id);

        return ResponseEntity.ok("Incident deleted successfully");
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {

        Incident incident = incidentService.getIncidentById(id);

        if (incident == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(incident);
    }

    // INCIDENT HISTORY
    @GetMapping("/{id}/history")
    public ResponseEntity<?> getIncidentHistory(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        User user = userRepository.findByEmail(email).orElse(null);

        Incident incident = incidentService.getIncidentById(id);

        if (incident == null) {
            return ResponseEntity.notFound().build();
        }

        boolean isAdmin = user != null &&
                user.getRole().name().equals("ADMIN");

        boolean isOfficer = user != null &&
                user.getRole().name().equals("OFFICER");

        boolean isOwner = incident.getCreatedBy() != null &&
                incident.getCreatedBy().getEmail().equals(email);

        if (!isAdmin && !isOfficer && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to view this history");
        }

        return ResponseEntity.ok(
                historyService.getHistoryByIncidentId(id)
        );
    }

    // USER UPDATE INCIDENT
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateIncident(
            @PathVariable Long id,
            @RequestBody IncidentDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        Incident existing = incidentService.getIncidentById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        String oldStatus = existing.getStatus();

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        User user = userRepository.findByEmail(email).orElse(null);

        String updatedBy =
                (user != null) ? user.getFullName() : "Unknown";

        Incident updated = incidentService.updateIncident(id, dto);

        if (updated == null) {
            return ResponseEntity.status(500)
                    .body("Update failed");
        }

        historyService.saveHistory(
                existing,
                oldStatus,
                updated.getStatus() != null
                        ? updated.getStatus()
                        : oldStatus,
                updatedBy,
                email,
                (user != null ? user.getRole().name() : "USER"),
                "User updated incident",
                "UPDATED"
        );

        return ResponseEntity.ok(updated);
    }

    // OFFICER UPDATE STATUS
    @PutMapping("/officer/update-status/{id}")
    public ResponseEntity<?> updateStatusByOfficer(
            @PathVariable Long id,
            @RequestBody IncidentDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String officerEmail =
                jwtService.extractUsername(token);

        incidentService.updateStatusByOfficer(
                id,
                dto.getStatus(),
                dto.getOfficerRemark(),
                officerEmail
        );

        return ResponseEntity.ok(
                "Incident status updated successfully"
        );
    }

    // ADMIN ASSIGN OFFICER
    @PutMapping("/assign")
    public ResponseEntity<?> assignOfficer(
            @RequestBody AssignOfficerDTO dto) {

        return ResponseEntity.ok(
                incidentService.assignOfficer(dto)
        );
    }
}

