package com.harsh.SITIMS.repository;

import com.harsh.SITIMS.entity.Tip;
import com.harsh.SITIMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipRepository extends JpaRepository<Tip, Long> {

    // ✅ OFFICER ASSIGNED TIPS (keep this)
    List<Tip> findByAssignedOfficer(User officer);

    // 🔥 NEW FIX: fetch tips by userId directly (IMPORTANT)
    List<Tip> findByInformantUserId(Long userId);
}