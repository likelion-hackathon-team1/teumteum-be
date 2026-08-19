package com.likelion.teumteum.repository;

import com.likelion.teumteum.entity.health.GlucoseAnomalyEvent;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlucoseAnomalyEventRepository extends JpaRepository<GlucoseAnomalyEvent, Long> {

  boolean existsByMemberIdAndDetectedAtBetween(Long memberId, LocalDateTime start, LocalDateTime end);
}
