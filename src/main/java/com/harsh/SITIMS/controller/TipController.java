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

    @PostMapping("/submit")
    public ResponseEntity<TipDTO> submitTip(@RequestBody TipDTO dto) {
        return ResponseEntity.ok(tipService.submitTip(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TipDTO>> getAllTips() {
        return ResponseEntity.ok(tipService.getAllTips());
    }

    @GetMapping("/assigned")
    public ResponseEntity<List<TipDTO>> getAssignedTips(@RequestParam Long officerId) {
        return ResponseEntity.ok(tipService.getTipsForOfficer(officerId));
    }

    @PutMapping("/{tipId}/assign/{officerId}")
    public ResponseEntity<TipDTO> assignTip(@PathVariable Long tipId, @PathVariable Long officerId) {
        return ResponseEntity.ok(tipService.assignTipToOfficer(tipId, officerId));
    }

    @PutMapping("/{tipId}/convert-to-incident")
    public ResponseEntity<TipDTO> convertTip(@PathVariable Long tipId) {
        return ResponseEntity.ok(tipService.convertTipToIncident(tipId));
    }

    @PutMapping("/{tipId}/link/{incidentId}")
    public ResponseEntity<TipDTO> linkTip(@PathVariable Long tipId, @PathVariable Long incidentId) {
        return ResponseEntity.ok(tipService.linkTipToIncident(tipId, incidentId));
    }

    @DeleteMapping("/{tipId}/ignore")
    public ResponseEntity<?> ignoreTip(@PathVariable Long tipId) {
        tipService.ignoreTip(tipId);
        return ResponseEntity.ok().build();
    }
}
