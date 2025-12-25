package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.dto.ThreatDTO;
import com.harsh.SITIMS.entity.Threat;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.repository.ThreatRepository;
import com.harsh.SITIMS.repository.UserRepository;
import com.harsh.SITIMS.service.ThreatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThreatServiceImpl implements ThreatService {

    private final ThreatRepository threatRepository;
    private final UserRepository userRepository;

    @Override
    public ThreatDTO createThreat(ThreatDTO dto) {
        Threat threat = new Threat();
        threat.setTitle(dto.getTitle());
        threat.setDescription(dto.getDescription());
        threat.setSeverity(dto.getSeverity());
        threat.setStatus(dto.getStatus() != null ? dto.getStatus() : "Pending");
        threat.setLocation(dto.getLocation());
        threat.setCreatedAt(LocalDateTime.now());

        if(dto.getReportedById() != null) {
            userRepository.findById(dto.getReportedById())
                    .ifPresent(threat::setReportedBy);
        }

        if(dto.getAssignedOfficerId() != null) {
            userRepository.findById(dto.getAssignedOfficerId())
                    .ifPresent(threat::setAssignedOfficer);
        }

        Threat saved = threatRepository.save(threat);
        dto.setId(saved.getId());
        return dto;
    }

    @Override
    public List<ThreatDTO> getAllThreats() {
        return threatRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ThreatDTO getThreatById(Long id) {
        return threatRepository.findById(id).map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Threat not found"));
    }

    @Override
    public ThreatDTO updateThreat(Long id, ThreatDTO dto) {
        Threat threat = threatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Threat not found"));

        threat.setTitle(dto.getTitle());
        threat.setDescription(dto.getDescription());
        threat.setSeverity(dto.getSeverity());
        threat.setStatus(dto.getStatus());
        threat.setLocation(dto.getLocation());

        if(dto.getAssignedOfficerId() != null) {
            userRepository.findById(dto.getAssignedOfficerId())
                    .ifPresent(threat::setAssignedOfficer);
        }

        threatRepository.save(threat);
        return convertToDTO(threat);
    }

    @Override
    public void deleteThreat(Long id) {
        threatRepository.deleteById(id);
    }

    private ThreatDTO convertToDTO(Threat threat) {
        ThreatDTO dto = new ThreatDTO();
        dto.setId(threat.getId());
        dto.setTitle(threat.getTitle());
        dto.setDescription(threat.getDescription());
        dto.setSeverity(threat.getSeverity());
        dto.setStatus(threat.getStatus());
        dto.setLocation(threat.getLocation());
        dto.setCreatedAt(threat.getCreatedAt());
        dto.setReportedById(threat.getReportedBy() != null ? threat.getReportedBy().getId() : null);
        dto.setAssignedOfficerId(threat.getAssignedOfficer() != null ? threat.getAssignedOfficer().getId() : null);
        return dto;
    }
}
