package com.harsh.SITIMS.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Remark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;                 // remark message

    private LocalDateTime createdAt;     // timestamp

    // Each remark belongs to 1 incident
    @ManyToOne
    @JoinColumn(name = "incident_id")
    private Incident incident;

    // Which officer added the remark
    @ManyToOne
    @JoinColumn(name = "officer_id")
    private User officer;
}
