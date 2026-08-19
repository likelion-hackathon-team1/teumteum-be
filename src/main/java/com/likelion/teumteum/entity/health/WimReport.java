package com.likelion.teumteum.entity.health;

import com.likelion.teumteum.entity.health.enums.WimLevel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "wim_report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WimReport {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "external_report_id")
  private String externalReportId;

  @Column
  private Integer wimScore;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private WimLevel exerciseLevel;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private WimLevel dailyLifeLevel;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private WimLevel sleepLevel;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private WimLevel mentalHealthLevel;

  private Integer weeklyExerciseMinutes;

  private Integer averageDailySteps;

  private Integer sleepMinutes;

  @Column(precision = 5, scale = 2)
  private BigDecimal sleepEfficiency;

  private Integer anxietyScore;

  private Integer depressionScore;

  private Integer stressScore;

  @Column(nullable = false)
  private LocalDate measuredAt;

  @Column(nullable = false)
  private LocalDateTime syncedAt;

  public static WimReport of(Long memberId, String externalReportId, Integer wimScore,
      WimLevel exerciseLevel, WimLevel dailyLifeLevel, WimLevel sleepLevel, WimLevel mentalHealthLevel,
      Integer weeklyExerciseMinutes, Integer averageDailySteps, Integer sleepMinutes,
      BigDecimal sleepEfficiency, Integer anxietyScore, Integer depressionScore, Integer stressScore,
      LocalDate measuredAt, LocalDateTime syncedAt) {
    WimReport report = new WimReport();
    report.memberId = memberId;
    report.externalReportId = externalReportId;
    report.wimScore = wimScore;
    report.exerciseLevel = exerciseLevel;
    report.dailyLifeLevel = dailyLifeLevel;
    report.sleepLevel = sleepLevel;
    report.mentalHealthLevel = mentalHealthLevel;
    report.weeklyExerciseMinutes = weeklyExerciseMinutes;
    report.averageDailySteps = averageDailySteps;
    report.sleepMinutes = sleepMinutes;
    report.sleepEfficiency = sleepEfficiency;
    report.anxietyScore = anxietyScore;
    report.depressionScore = depressionScore;
    report.stressScore = stressScore;
    report.measuredAt = measuredAt;
    report.syncedAt = syncedAt;
    return report;
  }
}
