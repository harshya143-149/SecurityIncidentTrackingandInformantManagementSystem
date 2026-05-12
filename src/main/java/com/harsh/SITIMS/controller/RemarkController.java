package com.harsh.SITIMS.controller;

import com.harsh.SITIMS.dto.RemarkDTO;
import com.harsh.SITIMS.service.impl.UserDetailsImpl;
import com.harsh.SITIMS.service.RemarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/remarks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class RemarkController {

    private final RemarkService remarkService;

    // =========================
    // ADD REMARK
    // =========================
    @PostMapping("/add")
    public ResponseEntity<RemarkDTO> addRemark(
            @RequestBody RemarkDTO dto,
            @AuthenticationPrincipal UserDetailsImpl officer) {

        RemarkDTO saved =
                remarkService.addRemark(dto, officer.getUsername());

        return ResponseEntity.ok(saved);
    }

    // =========================
    // GET REMARKS BY INCIDENT
    // =========================
    @GetMapping("/incident/{id}")
    public ResponseEntity<List<RemarkDTO>> getRemarks(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                remarkService.getRemarksForIncident(id)
        );
    }

    // =========================
    // UPDATE REMARK
    // =========================
    @PutMapping("/update/{remarkId}")
    public ResponseEntity<RemarkDTO> updateRemark(
            @PathVariable Long remarkId,
            @RequestParam String text) {

        RemarkDTO updated =
                remarkService.updateRemark(remarkId, text);

        return ResponseEntity.ok(updated);
    }

    // =========================
    // DELETE REMARK
    // =========================
    @DeleteMapping("/delete/{remarkId}")
    public ResponseEntity<String> deleteRemark(
            @PathVariable Long remarkId) {

        remarkService.deleteRemark(remarkId);

        return ResponseEntity.ok(
                "Remark deleted successfully"
        );
    }
}