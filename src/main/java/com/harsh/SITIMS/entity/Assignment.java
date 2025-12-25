package com.harsh.SITIMS.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
@Data
@NoArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // the incident assigned
    @ManyToOne
    @JoinColumn(name = "incident_id")
    private Incident incident;

    // the officer
    @ManyToOne
    @JoinColumn(name = "officer_id")
    private User officer;

    private LocalDateTime assignedAt;
}

