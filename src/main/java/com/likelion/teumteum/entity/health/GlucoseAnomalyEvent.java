package com.likelion.teumteum.entity.health;

import com.likelion.teumteum.entity.health.enums.DayContext;
import com.likelion.teumteum.entity.health.enums.DurationType;
import com.likelion.teumteum.entity.health.enums.OccurrenceContext;
import com.likelion.teumteum.entity.health.enums.RiseSpeed;
import com.likelion.teumteum.entity.health.enums.Severity;
import com.likelion.teumteum.entity.health.enums.TimeContext;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "glucose_anomaly_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlucoseAnomalyEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "baseline_id", nullable = false)
  private Long baselineId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Severity severity;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RiseSpeed riseSpeed;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DurationType durationType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OccurrenceContext occurrenceContext;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TimeContext timeContext;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DayContext dayContext;

  @Column(nullable = false)
  private LocalDateTime detectedAt;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  public static GlucoseAnomalyEvent of(Long memberId, Long baselineId, Severity severity,
      RiseSpeed riseSpeed, DurationType durationType, OccurrenceContext occurrenceContext,
      TimeContext timeContext, DayContext dayContext, LocalDateTime detectedAt, LocalDateTime createdAt) {
    GlucoseAnomalyEvent event = new GlucoseAnomalyEvent();
    event.memberId = memberId;
    event.baselineId = baselineId;
    event.severity = severity;
    event.riseSpeed = riseSpeed;
    event.durationType = durationType;
    event.occurrenceContext = occurrenceContext;
    event.timeContext = timeContext;
    event.dayContext = dayContext;
    event.detectedAt = detectedAt;
    event.createdAt = createdAt;
    return event;
  }
}
