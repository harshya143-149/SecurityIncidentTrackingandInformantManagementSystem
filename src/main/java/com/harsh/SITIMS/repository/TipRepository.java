package com.harsh.SITIMS.repository;

import com.harsh.SITIMS.entity.Tip;
import com.harsh.SITIMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TipRepository extends JpaRepository<Tip, Long> {
    List<Tip> findByAssignedOfficer(User officer);
}
