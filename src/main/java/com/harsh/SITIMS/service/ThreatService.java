package com.harsh.SITIMS.service;

import com.harsh.SITIMS.dto.ThreatDTO;

import java.util.List;

public interface ThreatService {
    ThreatDTO createThreat(ThreatDTO threatDTO);
    List<ThreatDTO> getAllThreats();
    ThreatDTO getThreatById(Long id);
    ThreatDTO updateThreat(Long id, ThreatDTO threatDTO);
    void deleteThreat(Long id);
}
