package com.likelion.teumteum.service;

import com.likelion.teumteum.entity.health.GlucoseAnomalyEvent;
import com.likelion.teumteum.entity.health.GlucoseBaseline;
import com.likelion.teumteum.entity.health.GlucoseData;
import com.likelion.teumteum.entity.health.enums.DayContext;
import com.likelion.teumteum.entity.health.enums.DurationType;
import com.likelion.teumteum.entity.health.enums.OccurrenceContext;
import com.likelion.teumteum.entity.health.enums.RiseSpeed;
import com.likelion.teumteum.entity.health.enums.Severity;
import com.likelion.teumteum.entity.health.enums.TimeContext;
import com.likelion.teumteum.repository.GlucoseAnomalyEventRepository;
import com.likelion.teumteum.repository.GlucoseBaselineRepository;
import com.likelion.teumteum.repository.GlucoseDataRepository;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GlucoseAnomalyDetectionService {

  private static final BigDecimal MILD_THRESHOLD_RATIO = BigDecimal.valueOf(1.2);
  private static final BigDecimal MODERATE_THRESHOLD_RATIO = BigDecimal.valueOf(1.4);
  private static final int LOOKBACK_MINUTES = 15;
  private static final int RAPID_RISE_MG_PER_MINUTE = 2;

  private final GlucoseBaselineRepository glucoseBaselineRepository;
  private final GlucoseDataRepository glucoseDataRepository;
  private final GlucoseAnomalyEventRepository glucoseAnomalyEventRepository;

  @Transactional
  public Optional<GlucoseAnomalyEvent> detectAndLabel(Long memberId, GlucoseData currentReading) {
    Optional<GlucoseBaseline> baselineOptional =
        glucoseBaselineRepository.findTopByMemberIdOrderByCalculatedAtDesc(memberId);
    if (baselineOptional.isEmpty()) {
      return Optional.empty();
    }
    GlucoseBaseline baseline = baselineOptional.get();

    BigDecimal currentValue = BigDecimal.valueOf(currentReading.getGlucoseValue());
    BigDecimal mildThreshold = baseline.getBaselineValue().multiply(MILD_THRESHOLD_RATIO);
    BigDecimal moderateThreshold = baseline.getBaselineValue().multiply(MODERATE_THRESHOLD_RATIO);

    if (currentValue.compareTo(mildThreshold) < 0) {
      return Optional.empty();
    }

    Severity severity = currentValue.compareTo(moderateThreshold) >= 0 ? Severity.MODERATE : Severity.MILD;

    List<GlucoseData> lookbackReadings = glucoseDataRepository.findByUserIdAndMeasuredAtBetween(
            memberId, currentReading.getMeasuredAt().minusMinutes(LOOKBACK_MINUTES), currentReading.getMeasuredAt())
        .stream()
        .filter(data -> data.getMeasuredAt().isBefore(currentReading.getMeasuredAt()))
        .toList();

    RiseSpeed riseSpeed = resolveRiseSpeed(currentReading, lookbackReadings);
    DurationType durationType = resolveDurationType(lookbackReadings, mildThreshold);
    OccurrenceContext occurrenceContext = resolveOccurrenceContext(memberId, currentReading.getMeasuredAt());
    TimeContext timeContext = resolveTimeContext(currentReading.getMeasuredAt());
    DayContext dayContext = resolveDayContext(currentReading.getMeasuredAt());

    GlucoseAnomalyEvent event = GlucoseAnomalyEvent.of(
        memberId, baseline.getId(), severity, riseSpeed, durationType,
        occurrenceContext, timeContext, dayContext,
        currentReading.getMeasuredAt(), LocalDateTime.now());

    return Optional.of(glucoseAnomalyEventRepository.save(event));
  }

  private RiseSpeed resolveRiseSpeed(GlucoseData currentReading, List<GlucoseData> lookbackReadings) {
    Optional<GlucoseData> previous = lookbackReadings.stream()
        .max(Comparator.comparing(GlucoseData::getMeasuredAt));

    if (previous.isEmpty()) {
      return RiseSpeed.GRADUAL;
    }

    long minutesBetween = Math.max(1,
        Duration.between(previous.get().getMeasuredAt(), currentReading.getMeasuredAt()).toMinutes());
    int diff = currentReading.getGlucoseValue() - previous.get().getGlucoseValue();
    double ratePerMinute = (double) diff / minutesBetween;

    return ratePerMinute >= RAPID_RISE_MG_PER_MINUTE ? RiseSpeed.RAPID : RiseSpeed.GRADUAL;
  }

  private DurationType resolveDurationType(List<GlucoseData> lookbackReadings, BigDecimal mildThreshold) {
    if (lookbackReadings.isEmpty()) {
      return DurationType.TEMPORARY;
    }
    boolean allElevated = lookbackReadings.stream()
        .allMatch(data -> BigDecimal.valueOf(data.getGlucoseValue()).compareTo(mildThreshold) >= 0);
    return allElevated ? DurationType.SUSTAINED : DurationType.TEMPORARY;
  }

  private OccurrenceContext resolveOccurrenceContext(Long memberId, LocalDateTime measuredAt) {
    LocalDateTime dayStart = measuredAt.toLocalDate().atStartOfDay();
    LocalDateTime dayEnd = dayStart.plusDays(1);
    boolean existsToday = glucoseAnomalyEventRepository.existsByMemberIdAndDetectedAtBetween(memberId, dayStart, dayEnd);
    return existsToday ? OccurrenceContext.REPEATED_TODAY : OccurrenceContext.FIRST_TODAY;
  }

  private TimeContext resolveTimeContext(LocalDateTime measuredAt) {
    int hour = measuredAt.getHour();
    if (hour >= 6 && hour < 18) {
      return TimeContext.DAYTIME;
    }
    if (hour >= 18 && hour < 22) {
      return TimeContext.EVENING;
    }
    return TimeContext.LATE_NIGHT;
  }

  private DayContext resolveDayContext(LocalDateTime measuredAt) {
    DayOfWeek dayOfWeek = measuredAt.getDayOfWeek();
    return (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)
        ? DayContext.WEEKEND : DayContext.WEEKDAY;
  }
}
