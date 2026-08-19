package com.likelion.teumteum.entity.health;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "glucose_baseline")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlucoseBaseline {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal baselineValue;

  @Column(nullable = false)
  private LocalDateTime calculationStartAt;

  @Column(nullable = false)
  private LocalDateTime calculationEndAt;

  @Column(nullable = false)
  private Integer sampleCount;

  @Column(nullable = false)
  private LocalDateTime calculatedAt;

  public static GlucoseBaseline of(Long memberId, BigDecimal baselineValue,
      LocalDateTime calculationStartAt, LocalDateTime calculationEndAt,
      Integer sampleCount, LocalDateTime calculatedAt) {
    GlucoseBaseline baseline = new GlucoseBaseline();
    baseline.memberId = memberId;
    baseline.baselineValue = baselineValue;
    baseline.calculationStartAt = calculationStartAt;
    baseline.calculationEndAt = calculationEndAt;
    baseline.sampleCount = sampleCount;
    baseline.calculatedAt = calculatedAt;
    return baseline;
  }
}
