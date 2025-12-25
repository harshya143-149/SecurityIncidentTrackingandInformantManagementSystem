package com.harsh.SITIMS.dto;

import lombok.Data;

// Simple DTO for officer update payload
@Data
public class OfficerUpdateDTO {
    private Long incidentId;
    private String status;
    private String remark;
    private String officerEmail; // <-- added field for officer email
}
