package com.harsh.SITIMS.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TipDTO {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String category;
    private String priority;

    private boolean anonymous;

    private String informantName;
    private String informantContact;

    private String status;
    private LocalDateTime tipDate;

    private Long assignedOfficerId;
    private String assignedOfficerName;

    private Long linkedIncidentId;

    // 🔥 IMPORTANT
    private Long informantUserId;
}