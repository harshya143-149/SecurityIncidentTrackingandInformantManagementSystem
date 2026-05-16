package com.harsh.SITIMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentDTO {

    private String title;
    private String description;
    private String severity;
    private String location;

    private Long assignedOfficerId;
    private Long linkedTipId;

    private String category;

    private String status;

    // ✅ ADD THIS
    private String officerRemark;

    public IncidentDTO(
            String title,
            String description,
            String category,
            String location
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
    }
}