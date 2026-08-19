package com.likelion.teumteum.config;

import com.likelion.teumteum.entity.health.GlucoseBaseline;
import com.likelion.teumteum.entity.health.GlucoseData;
import com.likelion.teumteum.entity.member.Member;
import com.likelion.teumteum.repository.GlucoseBaselineRepository;
import com.likelion.teumteum.repository.GlucoseDataRepository;
import com.likelion.teumteum.repository.MemberRepository;
import com.likelion.teumteum.service.GlucoseAnomalyDetectionService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GlucoseDataStreamSimulator {

  private static final int NORMAL_MIN_GLUCOSE = 70;
  private static final int NORMAL_MAX_GLUCOSE = 140;
  private static final int SPIKE_PROBABILITY_PERCENT = 12;
  private static final double SPIKE_MIN_RATIO = 1.5;
  private static final double SPIKE_MAX_RATIO = 1.9;
  private static final int FALLBACK_SPIKE_MIN_GLUCOSE = 180;
  private static final int FALLBACK_SPIKE_MAX_GLUCOSE = 220;

  private final MemberRepository memberRepository;
  private final GlucoseDataRepository glucoseDataRepository;
  private final GlucoseBaselineRepository glucoseBaselineRepository;
  private final GlucoseAnomalyDetectionService glucoseAnomalyDetectionService;

  @Scheduled(fixedRate = 5 * 60 * 1000)
  @Transactional
  public void streamGlucoseData() {
    for (Member member : memberRepository.findAll()) {
      GlucoseData saved = glucoseDataRepository.save(generateReading(member.getId()));

      Optional<?> anomalyEvent = glucoseAnomalyDetectionService.detectAndLabel(member.getId(), saved);
      if (anomalyEvent.isPresent()) {
        log.info("멤버 id={} 혈당값={} 에서 이상치 감지 및 라벨링 완료", member.getId(), saved.getGlucoseValue());
      }
    }
  }

  private GlucoseData generateReading(Long memberId) {
    LocalDateTime measuredAt = LocalDateTime.now();
    boolean isSpike = ThreadLocalRandom.current().nextInt(100) < SPIKE_PROBABILITY_PERCENT;

    int glucoseValue = isSpike ? generateSpikeValue(memberId) : generateNormalValue();

    return GlucoseData.of(memberId, glucoseValue, measuredAt);
  }

  private int generateNormalValue() {
    return ThreadLocalRandom.current().nextInt(NORMAL_MIN_GLUCOSE, NORMAL_MAX_GLUCOSE + 1);
  }

  private int generateSpikeValue(Long memberId) {
    Optional<GlucoseBaseline> baseline =
        glucoseBaselineRepository.findTopByMemberIdOrderByCalculatedAtDesc(memberId);

    if (baseline.isEmpty()) {
      return ThreadLocalRandom.current().nextInt(FALLBACK_SPIKE_MIN_GLUCOSE, FALLBACK_SPIKE_MAX_GLUCOSE + 1);
    }

    double ratio = ThreadLocalRandom.current().nextDouble(SPIKE_MIN_RATIO, SPIKE_MAX_RATIO);
    BigDecimal spikeValue = baseline.get().getBaselineValue().multiply(BigDecimal.valueOf(ratio));
    return spikeValue.intValue();
  }
}
