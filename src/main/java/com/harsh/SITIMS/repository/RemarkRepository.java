package com.harsh.SITIMS.repository;

import com.harsh.SITIMS.entity.Incident;
import com.harsh.SITIMS.entity.Remark;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RemarkRepository extends JpaRepository<Remark, Long> {
    List<Remark> findByIncidentOrderByCreatedAtDesc(Incident incident);
}
