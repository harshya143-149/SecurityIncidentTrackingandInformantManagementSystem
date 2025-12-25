package com.harsh.SITIMS.controller;

import com.harsh.SITIMS.dto.RemarkDTO;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.service.RemarkService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/remarks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RemarkController {

    private final RemarkService remarkService;

    @PostMapping("/add")
    public RemarkDTO addRemark(@RequestBody RemarkDTO dto,
                               @AuthenticationPrincipal User officer) {

        return remarkService.addRemark(dto, officer.getEmail());
    }

    @GetMapping("/incident/{id}")
    public List<RemarkDTO> getRemarks(@PathVariable Long id) {
        return remarkService.getRemarksForIncident(id);
    }
}
