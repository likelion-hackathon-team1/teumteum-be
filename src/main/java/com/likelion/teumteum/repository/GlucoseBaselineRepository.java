package com.likelion.teumteum.repository;

import com.likelion.teumteum.entity.health.GlucoseBaseline;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlucoseBaselineRepository extends JpaRepository<GlucoseBaseline, Long> {

  Optional<GlucoseBaseline> findTopByMemberIdOrderByCalculatedAtDesc(Long memberId);
}
