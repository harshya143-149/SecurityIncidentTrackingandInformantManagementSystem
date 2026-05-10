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
@CrossOrigin(origins = "http://localhost:8080") // ✅ FIXED
public class RemarkController {

    private final RemarkService remarkService;

    @PostMapping("/add")
    public ResponseEntity<RemarkDTO> addRemark(
            @RequestBody RemarkDTO dto,
            @AuthenticationPrincipal UserDetailsImpl officer) {
        RemarkDTO saved = remarkService.addRemark(dto, officer.getUsername());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/incident/{id}")
    public ResponseEntity<List<RemarkDTO>> getRemarks(@PathVariable Long id) {
        return ResponseEntity.ok(remarkService.getRemarksForIncident(id));
    }
}
