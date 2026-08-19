package com.likelion.teumteum.repository;

import com.likelion.teumteum.entity.health.WimReport;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WimReportRepository extends JpaRepository<WimReport, Long> {

  boolean existsByMemberId(Long memberId);

  Optional<WimReport> findFirstByMemberIdOrderByMeasuredAtDesc(Long memberId);
}
