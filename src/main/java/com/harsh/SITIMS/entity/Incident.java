package com.harsh.SITIMS.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String status;  // PENDING / ASSIGNED / RESOLVED
    private String location;
    private String severity;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;  // Automatically set when the record is created

    @Column(name = "reported_by")
    private String reportedBy;

    // Correct relationship for assigned officer
    @ManyToOne
    @JoinColumn(name = "assigned_officer_id")
    private User assignedOfficer;

    private Long linkedTipId;
    private String officerRemark;

    private String category;

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }
}
