package com.harsh.SITIMS.service;

import com.harsh.SITIMS.dto.TipDTO;
import java.util.List;

public interface TipService {
    TipDTO submitTip(TipDTO dto);
    List<TipDTO> getAllTips();
    List<TipDTO> getTipsForOfficer(Long officerId);
    TipDTO assignTipToOfficer(Long tipId, Long officerId);
    TipDTO convertTipToIncident(Long tipId);
    TipDTO linkTipToIncident(Long tipId, Long incidentId);
    TipDTO ignoreTip(Long tipId);
}
