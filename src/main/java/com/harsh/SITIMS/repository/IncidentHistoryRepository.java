package com.harsh.SITIMS.repository;

import com.harsh.SITIMS.entity.Incident;
import com.harsh.SITIMS.entity.IncidentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentHistoryRepository extends JpaRepository<IncidentHistory, Long> {

    // Get all history for one incident — newest first
    List<IncidentHistory> findByIncidentOrderByChangedAtDesc(Incident incident);

    // Get all history for incident by ID
    List<IncidentHistory> findByIncidentIdOrderByChangedAtDesc(Long incidentId);
}