package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.dto.TipDTO;
import com.harsh.SITIMS.entity.Informant;
import com.harsh.SITIMS.entity.Tip;
import com.harsh.SITIMS.entity.User;
import com.harsh.SITIMS.entity.Incident;
import com.harsh.SITIMS.repository.TipRepository;
import com.harsh.SITIMS.repository.InformantRepository;
import com.harsh.SITIMS.repository.UserRepository;
import com.harsh.SITIMS.repository.IncidentRepository;
import com.harsh.SITIMS.service.TipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipServiceImpl implements TipService {

    private final TipRepository tipRepository;
    private final InformantRepository informantRepository;
    private final UserRepository userRepository;
    private final IncidentRepository incidentRepository;

    @Override
    public TipDTO submitTip(TipDTO dto) {

        Tip tip = new Tip();
        tip.setTitle(dto.getTitle());
        tip.setAnonymous(dto.isAnonymous());
        tip.setLocation(dto.getLocation());
        tip.setDescription(dto.getDescription());
        tip.setCategory(dto.getCategory());
        tip.setPriority(dto.getPriority());
        tip.setStatus("PENDING");

        // If tip is not anonymous → save informant
        if (!dto.isAnonymous() &&
                dto.getInformantName() != null &&
                !dto.getInformantName().isEmpty()) {

            Informant informant = new Informant();
            informant.setName(dto.getInformantName());
            informant.setPhone(dto.getInformantContact());
            informant = informantRepository.save(informant);

            tip.setInformant(informant);
            tip.setInformantName(informant.getName());
            tip.setInformantContact(informant.getPhone());

        } else {
            tip.setInformant(null);
            tip.setInformantName("Anonymous");
            tip.setInformantContact("-");
        }

        Tip saved = tipRepository.save(tip);
        return toDTO(saved);
    }

    @Override
    public List<TipDTO> getAllTips() {
        return tipRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TipDTO> getTipsForOfficer(Long officerId) {
        User officer = userRepository.findById(officerId)
                .orElseThrow(() -> new RuntimeException("Officer not found"));

        return tipRepository.findByAssignedOfficer(officer)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TipDTO assignTipToOfficer(Long tipId, Long officerId) {

        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new RuntimeException("Tip not found"));

        User officer = userRepository.findById(officerId)
                .orElseThrow(() -> new RuntimeException("Officer not found"));

        tip.setAssignedOfficer(officer);
        tip.setStatus("ASSIGNED");

        tipRepository.save(tip);
        return toDTO(tip);
    }

    @Override
    public TipDTO convertTipToIncident(Long tipId) {

        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new RuntimeException("Tip not found"));

        // Prevent duplicate incident creation
        if (tip.getLinkedIncident() != null) {
            throw new RuntimeException("Incident already created for this tip");
        }

        // Prevent converting ignored tips
        if ("IGNORED".equalsIgnoreCase(tip.getStatus())) {
            throw new RuntimeException("Cannot create incident for an ignored tip");
        }

        // Create a minimal incident with location and severity
        Incident incident = new Incident();
        incident.setTitle(tip.getTitle());
        incident.setDescription(tip.getDescription());

        // Map location and severity from tip
        incident.setLocation(tip.getLocation());
        incident.setSeverity(tip.getPriority()); // Assuming priority maps to severity

        incident = incidentRepository.save(incident);

        tip.setLinkedIncident(incident);
        tip.setStatus("INCIDENT_CREATED");

        tipRepository.save(tip);
        return toDTO(tip);
    }

    @Override
    public TipDTO linkTipToIncident(Long tipId, Long incidentId) {
        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new RuntimeException("Tip not found"));

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        tip.setLinkedIncident(incident);
        tip.setStatus("LINKED");

        tipRepository.save(tip);
        return toDTO(tip);
    }

    @Override
    public TipDTO ignoreTip(Long tipId) {

        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new RuntimeException("Tip not found"));

        tip.setStatus("IGNORED");

        tipRepository.save(tip);
        return toDTO(tip);
    }

    private TipDTO toDTO(Tip t) {

        TipDTO dto = new TipDTO();

        dto.setId(t.getId());
        dto.setTitle(t.getTitle());
        dto.setDescription(t.getDescription());
        dto.setLocation(t.getLocation());
        dto.setCategory(t.getCategory());
        dto.setPriority(t.getPriority());
        dto.setStatus(t.getStatus());
        dto.setAnonymous(t.isAnonymous());
        dto.setTipDate(t.getCreatedAt());

        dto.setInformantName(t.getInformantName());
        dto.setInformantContact(t.getInformantContact());

        // Officer mapping
        if (t.getAssignedOfficer() != null) {
            dto.setAssignedOfficerId(t.getAssignedOfficer().getId());
            dto.setAssignedOfficerName(t.getAssignedOfficer().getName());
        }

        // Linked incident
        if (t.getLinkedIncident() != null) {
            dto.setLinkedIncidentId(t.getLinkedIncident().getId());
        }

        return dto;
    }
}
