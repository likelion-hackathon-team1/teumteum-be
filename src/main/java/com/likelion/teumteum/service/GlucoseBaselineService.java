package com.likelion.teumteum.service;

import com.likelion.teumteum.entity.health.GlucoseBaseline;
import com.likelion.teumteum.entity.health.GlucoseData;
import com.likelion.teumteum.repository.GlucoseBaselineRepository;
import com.likelion.teumteum.repository.GlucoseDataRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GlucoseBaselineService {

  private static final int BASELINE_PERIOD_DAYS = 3;

  private final GlucoseDataRepository glucoseDataRepository;
  private final GlucoseBaselineRepository glucoseBaselineRepository;

  @Transactional
  public GlucoseBaseline calculateBaseline(Long memberId) {
    LocalDateTime end = LocalDateTime.now();
    LocalDateTime start = end.minusDays(BASELINE_PERIOD_DAYS);

    List<GlucoseData> glucoseDataList =
        glucoseDataRepository.findByUserIdAndMeasuredAtBetween(memberId, start, end);

    if (glucoseDataList.isEmpty()) {
      return null;
    }

    BigDecimal sum = glucoseDataList.stream()
        .map(GlucoseData::getGlucoseValue)
        .map(BigDecimal::valueOf)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal average = sum.divide(BigDecimal.valueOf(glucoseDataList.size()), 2, RoundingMode.HALF_UP);

    GlucoseBaseline baseline = GlucoseBaseline.of(
        memberId, average, start, end, glucoseDataList.size(), LocalDateTime.now());

    return glucoseBaselineRepository.save(baseline);
  }
}
