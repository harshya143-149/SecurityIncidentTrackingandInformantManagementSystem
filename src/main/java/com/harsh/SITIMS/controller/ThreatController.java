package com.harsh.SITIMS.controller;

import com.harsh.SITIMS.dto.ThreatDTO;
import com.harsh.SITIMS.service.ThreatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/threats")
@RequiredArgsConstructor
public class ThreatController {

    private final ThreatService threatService;

    @PostMapping
    public ResponseEntity<ThreatDTO> createThreat(@RequestBody ThreatDTO dto) {
        return ResponseEntity.ok(threatService.createThreat(dto));
    }

    @GetMapping
    public ResponseEntity<List<ThreatDTO>> getAllThreats() {
        return ResponseEntity.ok(threatService.getAllThreats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThreatDTO> getThreat(@PathVariable Long id) {
        return ResponseEntity.ok(threatService.getThreatById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ThreatDTO> updateThreat(@PathVariable Long id, @RequestBody ThreatDTO dto) {
        return ResponseEntity.ok(threatService.updateThreat(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteThreat(@PathVariable Long id) {
        threatService.deleteThreat(id);
        return ResponseEntity.ok("Threat deleted successfully");
    }
}
