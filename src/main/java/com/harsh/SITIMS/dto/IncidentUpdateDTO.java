package com.harsh.SITIMS.dto;

import lombok.Data;

@Data
public class IncidentUpdateDTO {

    private Long incidentId;   // ID of incident to update
    private String status;     // New status (PENDING, IN_PROGRESS, RESOLVED)
    private String text;       // Remark text (officer remark)
}
