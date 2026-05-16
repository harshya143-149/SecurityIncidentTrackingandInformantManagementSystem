package com.harsh.SITIMS.controller;

import com.harsh.SITIMS.dto.RemarkDTO;
import com.harsh.SITIMS.service.RemarkService;
import com.harsh.SITIMS.service.impl.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/remarks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RemarkController {

    private final RemarkService remarkService;

    // ADD
    @PostMapping("/add")
    public ResponseEntity<?> addRemark(
            @RequestBody RemarkDTO dto,
            @AuthenticationPrincipal UserDetailsImpl officer) {

        if (officer == null) {
            return ResponseEntity.badRequest().body("User not authenticated");
        }

        return ResponseEntity.ok(
                remarkService.addRemark(dto, officer.getUsername())
        );
    }

    // GET
    @GetMapping("/incident/{id}")
    public ResponseEntity<List<RemarkDTO>> getRemarks(@PathVariable Long id) {
        return ResponseEntity.ok(
                remarkService.getRemarksForIncident(id)
        );
    }

    // UPDATE (OWNER ONLY)
    @PutMapping("/update/{remarkId}")
    public ResponseEntity<?> updateRemark(
            @PathVariable Long remarkId,
            @RequestParam String text,
            @AuthenticationPrincipal UserDetailsImpl officer) {

        return ResponseEntity.ok(
                remarkService.updateRemark(
                        remarkId,
                        text,
                        officer.getUsername()
                )
        );
    }

    // DELETE (OWNER ONLY)
    @DeleteMapping("/delete/{remarkId}")
    public ResponseEntity<?> deleteRemark(
            @PathVariable Long remarkId,
            @AuthenticationPrincipal UserDetailsImpl officer) {

        remarkService.deleteRemark(
                remarkId,
                officer.getUsername()
        );

        return ResponseEntity.ok("Remark deleted successfully");
    }
}