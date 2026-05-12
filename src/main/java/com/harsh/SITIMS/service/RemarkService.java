package com.harsh.SITIMS.service;

import com.harsh.SITIMS.dto.RemarkDTO;

import java.util.List;

public interface RemarkService {

    RemarkDTO addRemark(RemarkDTO dto, String officerEmail);

    List<RemarkDTO> getRemarksForIncident(Long incidentId);

    List<RemarkDTO> getRemarksByIncidentId(Long incidentId);

    // UPDATE REMARK
    RemarkDTO updateRemark(Long remarkId, String text);

    // DELETE REMARK
    void deleteRemark(Long remarkId);
}