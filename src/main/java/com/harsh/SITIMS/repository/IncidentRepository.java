package com.harsh.SITIMS.repository;

import com.harsh.SITIMS.entity.Incident;
import com.harsh.SITIMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

    // Fetch incidents reported by a user (citizen)
    List<Incident> findByReportedBy(String email);

    // Fetch incidents assigned to an officer (using entity mapping)
    List<Incident> findByAssignedOfficer(User officer);

    // FETCH incident + officer info — used to avoid lazy loading errors
    @Query("SELECT i FROM Incident i LEFT JOIN FETCH i.assignedOfficer WHERE i.assignedOfficer.id = :officerId")
    List<Incident> findByAssignedOfficerId(@Param("officerId") Long officerId);

    // Admin use-case: Fetch all incidents with officer details
    @Query("SELECT i FROM Incident i LEFT JOIN FETCH i.assignedOfficer")
    List<Incident> findAllWithOfficer();
}
