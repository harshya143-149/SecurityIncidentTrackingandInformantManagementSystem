package com.harsh.SITIMS.controller;

import com.harsh.SITIMS.dto.OfficerUpdateDTO;
import com.harsh.SITIMS.dto.RemarkDTO;
import com.harsh.SITIMS.entity.Incident;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/officer")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OfficerController {

    private final UserService userService;
    private final IncidentService incidentService;
    private final AdminUserService adminUserService;
    private final RemarkService remarkService;
    private final OfficerService officerService;

    // ==============================
    // FETCH OFFICER BY EMAIL
    // ==============================
    @GetMapping("/by-email/{email}")
    public ResponseEntity<User> getOfficerByEmail(
            @PathVariable String email) {

        User officer =
                userService.getUserByEmailAndRole(
                        email,
                        "OFFICER"
                );

        if (officer != null) {
            return ResponseEntity.ok(officer);
        }

        return ResponseEntity.status(
                HttpStatus.NOT_FOUND
        ).body(null);
    }

    // ==============================
    // UPDATE OFFICER PROFILE
    // ==============================
    @PutMapping("/{id}")
    public ResponseEntity<User> updateOfficer(
            @PathVariable Long id,
            @RequestBody User officer) {

        User updatedOfficer =
                officerService.updateOfficer(id, officer);

        return ResponseEntity.ok(updatedOfficer);
    }

    // ==============================
    // FETCH ASSIGNED INCIDENTS
    // ==============================
    @GetMapping("/assigned/{officerId}")
    public ResponseEntity<List<Incident>> getAssignedIncidents(
            @PathVariable Long officerId) {

        List<Incident> incidents =
                incidentService.getIncidentsByOfficer(officerId);

        return ResponseEntity.ok(incidents);
    }

    // ==============================
    // UPDATE INCIDENT STATUS
    // ==============================
    @PostMapping("/update-status/{incidentId}")
    public ResponseEntity<String> updateIncidentStatus(
            @PathVariable Long incidentId,
            @RequestParam String status,
            @RequestParam(required = false) String remark,
            @RequestParam String officerEmail) {

        Incident incident =
                incidentService.getIncidentById(incidentId);

        if (incident == null) {
            return ResponseEntity.notFound().build();
        }

        // ✅ STATUS + HISTORY SAVED INSIDE SERVICE
        incidentService.updateStatusByOfficer(
                incidentId,
                status,
                remark,
                officerEmail
        );

        // ✅ OPTIONAL REMARK TABLE SAVE
        if (remark != null && !remark.trim().isEmpty()) {

            RemarkDTO remarkDTO = new RemarkDTO();

            remarkDTO.setIncidentId(incidentId);
            remarkDTO.setText(remark);

            remarkService.addRemark(
                    remarkDTO,
                    officerEmail
            );
        }

        return ResponseEntity.ok(
                "Status updated successfully"
        );
    }

    // ==============================
    // UPDATE INCIDENT USING DTO
    // ==============================
    @PostMapping("/update")
    public ResponseEntity<String> updateIncidentByOfficer(
            @RequestBody OfficerUpdateDTO dto,
            @RequestHeader(
                    value = "Authorization",
                    required = false
            ) String authHeader) {

        Incident incident =
                incidentService.getIncidentById(
                        dto.getIncidentId()
                );

        if (incident == null) {
            return ResponseEntity.notFound().build();
        }

        // ✅ STATUS + HISTORY SAVED INSIDE SERVICE
        incidentService.updateStatusByOfficer(
                dto.getIncidentId(),
                dto.getStatus(),
                dto.getRemark(),
                dto.getOfficerEmail()
        );

        // ✅ OPTIONAL REMARK TABLE SAVE
        if (dto.getRemark() != null
                && !dto.getRemark().trim().isEmpty()) {

            RemarkDTO remarkDTO = new RemarkDTO();

            remarkDTO.setIncidentId(dto.getIncidentId());
            remarkDTO.setText(dto.getRemark());

            remarkService.addRemark(
                    remarkDTO,
                    dto.getOfficerEmail()
            );
        }

        return ResponseEntity.ok(
                "Incident updated successfully"
        );
    }

    // ==============================
    // ADD OFFICER — ADMIN ONLY
    // ==============================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-officer")
    public ResponseEntity<?> addOfficer(
            @RequestBody User officer) {

        try {

            User savedOfficer =
                    adminUserService.addOfficer(officer);

            return ResponseEntity.ok(savedOfficer);

        } catch (Exception e) {

            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body(
                    "Failed to add officer: "
                            + e.getMessage()
            );
        }
    }

    // ==============================
    // FETCH REMARKS
    // ==============================
    @GetMapping("/remarks/incident/{incidentId}")
    public ResponseEntity<List<RemarkDTO>> getRemarksByIncident(
            @PathVariable Long incidentId) {

        List<RemarkDTO> remarks =
                remarkService.getRemarksByIncidentId(
                        incidentId
                );

        return ResponseEntity.ok(remarks);
    }
}