package com.harsh.SITIMS.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentDTO {

    private String title;
    private String description;
    private String severity;
    private String location;

    private Long assignedOfficerId;  // optional
    private Long linkedTipId;        // optional

    private String category; // if you want category support

    // Custom constructor for selected fields
    public IncidentDTO(String title, String description, String category, String location) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
    }
}
