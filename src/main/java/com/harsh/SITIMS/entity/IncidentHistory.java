package com.harsh.SITIMS.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "incident_history")
public class IncidentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which incident this history belongs to
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;

    // Who made the change
    @Column(columnDefinition = "TEXT")
    private String changedByName;

    @Column(columnDefinition = "TEXT")
    private String changedByEmail;

    @Column(columnDefinition = "TEXT")
    private String changedByRole;

    @Column(columnDefinition = "TEXT")
    private String oldStatus;

    @Column(columnDefinition = "TEXT")
    private String newStatus;

    @Column(columnDefinition = "TEXT")
    private String remark;

    // When it changed
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime changedAt;

    @Column(columnDefinition = "TEXT")
    private String actionType;
}