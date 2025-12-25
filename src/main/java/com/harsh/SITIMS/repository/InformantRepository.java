package com.harsh.SITIMS.repository;

import com.harsh.SITIMS.entity.Informant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InformantRepository extends JpaRepository<Informant, Long> {
    Optional<Informant> findByEmail(String email);
}
