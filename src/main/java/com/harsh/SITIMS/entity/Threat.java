package com.harsh.SITIMS.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "threats")
@Data
@NoArgsConstructor
public class Threat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String severity; // Low, Medium, High

    private String status; // Pending, Investigating, Resolved

    private String location;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "reported_by_user_id")
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "assigned_officer_id")
    private User assignedOfficer;
}