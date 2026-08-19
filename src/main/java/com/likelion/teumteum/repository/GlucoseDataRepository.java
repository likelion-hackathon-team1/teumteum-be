package com.likelion.teumteum.repository;

import com.likelion.teumteum.entity.health.GlucoseData;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlucoseDataRepository extends JpaRepository<GlucoseData, Long> {

  List<GlucoseData> findByUserIdAndMeasuredAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}
