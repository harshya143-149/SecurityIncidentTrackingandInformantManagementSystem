package com.harsh.SITIMS.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IncidentHistoryDTO {

    private Long id;
    private Long incidentId;

    // Who changed
    private String changedByName;
    private String changedByEmail;
    private String changedByRole;

    // What changed
    private String oldStatus;
    private String newStatus;

    // Why
    private String remark;

    // When
    private LocalDateTime changedAt;

    // Action type
    private String actionType;
}