package com.harsh.SITIMS.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tips")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean anonymous;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "informant_id")
    private Informant informant;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String location;

    @Column(columnDefinition = "TEXT")
    private String category;

    @Column(columnDefinition = "TEXT")
    private String priority;

    @Column(columnDefinition = "TEXT")
    private String status;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_officer_id")
    private User assignedOfficer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "linked_incident_id")
    private Incident linkedIncident;

    @Column(columnDefinition = "TEXT")
    private String informantName;

    @Column(columnDefinition = "TEXT")
    private String informantContact;

    // 🔥 IMPORTANT FIELD (USER LINK)
    @Column(name = "informant_user_id")
    private Long informantUserId;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.status == null) this.status = "PENDING";
        if (this.priority == null) this.priority = "LOW";
    }
}