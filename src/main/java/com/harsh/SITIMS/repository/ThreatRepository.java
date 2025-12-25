package com.harsh.SITIMS.repository;

import com.harsh.SITIMS.entity.Threat;
import com.harsh.SITIMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThreatRepository extends JpaRepository<Threat, Long> {
    List<Threat> findByAssignedOfficer(User officer);
    List<Threat> findByReportedBy(User user);
    List<Threat> findByStatus(String status);
}
