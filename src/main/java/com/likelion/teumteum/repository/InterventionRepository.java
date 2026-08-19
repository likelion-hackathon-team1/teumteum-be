package com.likelion.teumteum.repository;

import com.likelion.teumteum.entity.intervention.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterventionRepository extends JpaRepository<Intervention, Long> {
}
