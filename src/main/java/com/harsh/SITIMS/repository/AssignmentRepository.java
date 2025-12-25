package com.harsh.SITIMS.repository;

import com.harsh.SITIMS.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByOfficerId(Long officerId);
    List<Assignment> findByIncidentId(Long incidentId);
}
