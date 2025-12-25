package com.harsh.SITIMS.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThreatDTO {
    private Long id;
    private String title;
    private String description;
    private String severity;
    private String status;
    private String location;
    private LocalDateTime createdAt;
    private Long reportedById;
    private Long assignedOfficerId;
}
