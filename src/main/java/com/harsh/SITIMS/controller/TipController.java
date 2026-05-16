package com.harsh.SITIMS.controller;

import com.harsh.SITIMS.dto.TipDTO;
import com.harsh.SITIMS.service.TipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tips")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TipController {

    private final TipService tipService;

    // CREATE TIP
    @PostMapping("/submit")
    public ResponseEntity<TipDTO> submitTip(@RequestBody TipDTO dto) {
        return ResponseEntity.ok(tipService.submitTip(dto));
    }

    // GET ALL TIPS
    @GetMapping("/all")
    public ResponseEntity<List<TipDTO>> getAllTips() {
        return ResponseEntity.ok(tipService.getAllTips());
    }

    // GET USER TIPS
    @GetMapping("/my")
    public ResponseEntity<List<TipDTO>> getMyTips(@RequestParam Long userId) {
        return ResponseEntity.ok(tipService.getTipsByUser(userId));
    }

    // GET OFFICER ASSIGNED TIPS
    @GetMapping("/assigned")
    public ResponseEntity<List<TipDTO>> getAssignedTips(@RequestParam Long officerId) {
        return ResponseEntity.ok(tipService.getTipsForOfficer(officerId));
    }

    // ASSIGN TIP
    @PutMapping("/{tipId}/assign/{officerId}")
    public ResponseEntity<TipDTO> assignTip(@PathVariable Long tipId,
                                            @PathVariable Long officerId) {
        return ResponseEntity.ok(tipService.assignTipToOfficer(tipId, officerId));
    }

    // CONVERT TIP TO INCIDENT
    @PutMapping("/{tipId}/convert-to-incident")
    public ResponseEntity<TipDTO> convertTip(@PathVariable Long tipId) {
        return ResponseEntity.ok(tipService.convertTipToIncident(tipId));
    }

    // LINK TIP TO INCIDENT
    @PutMapping("/{tipId}/link/{incidentId}")
    public ResponseEntity<TipDTO> linkTip(@PathVariable Long tipId,
                                          @PathVariable Long incidentId) {
        return ResponseEntity.ok(tipService.linkTipToIncident(tipId, incidentId));
    }

    // IGNORE TIP
    @PutMapping("/{tipId}/ignore")
    public ResponseEntity<TipDTO> ignoreTip(@PathVariable Long tipId) {
        return ResponseEntity.ok(tipService.ignoreTip(tipId));
    }

    // UPDATE TIP
    @PutMapping("/{tipId}")
    public ResponseEntity<TipDTO> updateTip(@PathVariable Long tipId,
                                            @RequestBody TipDTO dto) {
        return ResponseEntity.ok(tipService.updateTip(tipId, dto));
    }

    // DELETE TIP ✅ FIXED FOR YOUR FRONTEND
    @DeleteMapping("/{tipId}")
    public ResponseEntity<String> deleteTip(@PathVariable Long tipId) {
        tipService.deleteTip(tipId);
        return ResponseEntity.ok("Tip deleted successfully");
    }
}