package com.harsh.SITIMS.service.impl;

import com.harsh.SITIMS.dto.TipDTO;
import com.harsh.SITIMS.entity.*;
import com.harsh.SITIMS.repository.*;
import com.harsh.SITIMS.service.TipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        tip.setDescription(dto.getDescription());
        tip.setLocation(dto.getLocation());
        tip.setCategory(dto.getCategory());
        tip.setPriority(dto.getPriority());
        tip.setAnonymous(dto.isAnonymous());

        tip.setInformantUserId(dto.getInformantUserId());

        if (!dto.isAnonymous()) {

            Informant informant = new Informant();
            informant.setName(dto.getInformantName());
            informant.setPhone(dto.getInformantContact());

            informantRepository.save(informant);

            tip.setInformantName(dto.getInformantName());
            tip.setInformantContact(dto.getInformantContact());

        } else {
            tip.setInformantName("Anonymous");
            tip.setInformantContact("-");
        }

        tip.setStatus("PENDING");

        return toDTO(tipRepository.save(tip));
    }

    @Override
    public List<TipDTO> getTipsByUser(Long userId) {

        return tipRepository.findAll()
                .stream()
                .filter(t -> t.getInformantUserId() != null
                        && t.getInformantUserId().equals(userId))
                .map(this::toDTO)
                .collect(Collectors.toList());
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

        return toDTO(tipRepository.save(tip));
    }

    @Override
    public TipDTO convertTipToIncident(Long tipId) {

        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new RuntimeException("Tip not found"));

        Incident incident = new Incident();
        incident.setTitle(tip.getTitle());
        incident.setDescription(tip.getDescription());
        incident.setLocation(tip.getLocation());
        incident.setSeverity(tip.getPriority());
        incident.setStatus("PENDING");
        incident.setReportedBy("TIP_SYSTEM");

        incident = incidentRepository.save(incident);

        tip.setLinkedIncident(incident);
        tip.setStatus("INCIDENT_CREATED");

        return toDTO(tipRepository.save(tip));
    }

    @Override
    public TipDTO linkTipToIncident(Long tipId, Long incidentId) {

        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new RuntimeException("Tip not found"));

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        tip.setLinkedIncident(incident);
        tip.setStatus("LINKED");

        return toDTO(tipRepository.save(tip));
    }

    @Override
    public TipDTO ignoreTip(Long tipId) {

        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new RuntimeException("Tip not found"));

        tip.setStatus("IGNORED");

        return toDTO(tipRepository.save(tip));
    }

    // =========================
    // FIXED UPDATE METHOD
    // =========================
    @Override
    @Transactional
    public TipDTO updateTip(Long tipId, TipDTO dto) {

        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new RuntimeException("Tip not found"));

        tip.setTitle(dto.getTitle());
        tip.setDescription(dto.getDescription());
        tip.setLocation(dto.getLocation());
        tip.setCategory(dto.getCategory());
        tip.setPriority(dto.getPriority());

        Tip updatedTip = tipRepository.saveAndFlush(tip);

        return toDTO(updatedTip);
    }

    // =========================
    // DELETE TIP
    // =========================
    @Override
    public void deleteTip(Long tipId) {

        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new RuntimeException("Tip not found"));

        tipRepository.delete(tip);
    }

    // =========================
    // DTO MAPPER
    // =========================
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
        dto.setInformantUserId(t.getInformantUserId());

        return dto;
    }
}