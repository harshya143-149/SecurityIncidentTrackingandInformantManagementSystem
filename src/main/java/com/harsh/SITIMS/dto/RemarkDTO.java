package com.harsh.SITIMS.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RemarkDTO {
    private Long id;
    private Long incidentId;
    private Long officerId;
    private String officerName;
    private String text;              // officer remark message
    private LocalDateTime createdAt;  // timestamp
}
