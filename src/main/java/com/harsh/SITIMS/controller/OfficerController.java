package com.harsh.SITIMS.controller;

import com.harsh.SITIMS.dto.OfficerUpdateDTO;
import com.harsh.SITIMS.dto.RemarkDTO;
import com.harsh.SITIMS.entity.Incident;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.service.AdminUserService;
import com.harsh.SITIMS.service.IncidentService;
import com.harsh.SITIMS.service.RemarkService;
import com.harsh.SITIMS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/officer")
@CrossOrigin
@RequiredArgsConstructor
public class OfficerController {

    private final UserService userService;
    private final IncidentService incidentService;
    private final AdminUserService adminUserService;
    private final RemarkService remarkService; // Added RemarkService

    // ==============================
    // FETCH OFFICER BY EMAIL
    // ==============================
    @GetMapping("/by-email/{email}")
    public ResponseEntity<User> getOfficerByEmail(@PathVariable String email) {
        User officer = userService.getUserByEmailAndRole(email, "OFFICER");
        if (officer != null) {
            return ResponseEntity.ok(officer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // ==============================
    // FETCH INCIDENTS ASSIGNED TO OFFICER
    // ==============================
    @GetMapping("/assigned/{officerId}")
    public ResponseEntity<List<Incident>> getAssignedIncidents(@PathVariable Long officerId) {
        List<Incident> incidents = incidentService.getIncidentsByOfficer(officerId);
        return ResponseEntity.ok(incidents);
    }

    // ==============================
    // UPDATE INCIDENT STATUS BY OFFICER (Query Params)
    // ==============================
    @PostMapping("/update-status/{incidentId}")
    public ResponseEntity<String> updateIncidentStatus(@PathVariable Long incidentId,
                                                       @RequestParam String status,
                                                       @RequestParam(required = false) String remark,
                                                       @RequestParam String officerEmail) {
        // Update incident status
        incidentService.updateStatusByOfficer(incidentId, status, remark);

        // Save remark if present
        if (remark != null && !remark.isEmpty()) {
            RemarkDTO remarkDTO = new RemarkDTO();
            remarkDTO.setIncidentId(incidentId);
            remarkDTO.setText(remark);
            remarkService.addRemark(remarkDTO, officerEmail);
        }

        return ResponseEntity.ok("Status updated and remark saved successfully");
    }

    // ==============================
    // UPDATE INCIDENT USING DTO (JSON Body)
    // ==============================
    @PostMapping("/update")
    public ResponseEntity<String> updateIncidentByOfficer(@RequestBody OfficerUpdateDTO dto,
                                                          @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // Update incident status
        incidentService.updateStatusByOfficer(dto.getIncidentId(), dto.getStatus(), dto.getRemark());

        // Save remark if present
        if (dto.getRemark() != null && !dto.getRemark().isEmpty()) {
            RemarkDTO remarkDTO = new RemarkDTO();
            remarkDTO.setIncidentId(dto.getIncidentId());
            remarkDTO.setText(dto.getRemark());

            // Use officerEmail from DTO
            remarkService.addRemark(remarkDTO, dto.getOfficerEmail());
        }

        return ResponseEntity.ok("Incident updated and remark saved");
    }

    // ==============================
    // ADD NEW OFFICER
    // ==============================
    @PostMapping("/add-officer")
    public ResponseEntity<?> addOfficer(@RequestBody User officer) {
        try {
            // Force role to OFFICER and encode password in service
            User savedOfficer = adminUserService.addOfficer(officer);
            return ResponseEntity.ok(savedOfficer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add officer: " + e.getMessage());
        }
    }

    // ==============================
    // FETCH ALL REMARKS FOR AN INCIDENT
    // ==============================
    @GetMapping("/remarks/incident/{incidentId}")
    public ResponseEntity<List<RemarkDTO>> getRemarksByIncident(@PathVariable Long incidentId) {
        List<RemarkDTO> remarks = remarkService.getRemarksByIncidentId(incidentId);
        return ResponseEntity.ok(remarks);
    }
}
