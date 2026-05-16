package com.harsh.SITIMS.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status;  // PENDING / ASSIGNED / RESOLVED
    private String location;
    private String severity;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "reported_by")
    private String reportedBy;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_officer_id")
    private User assignedOfficer;

    private Long linkedTipId;

    @Column(columnDefinition = "TEXT")
    private String officerRemark;

    private String category;

    // ✅ FIX: prevents Hibernate FK blocking + avoids eager loading issues
    @JsonManagedReference
    @OneToMany(
            mappedBy = "incident",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<IncidentHistory> historyList;

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }

    // ✅ SAFETY HOOK (prevents FK conflict during delete)
    @PreRemove
    private void preRemove() {
        if (historyList != null) {
            historyList.clear();
        }
    }
}