package com.likelion.teumteum.entity.health;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "glucose_data")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlucoseData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Integer glucoseValue;

  @Column(nullable = false)
  private LocalDateTime measuredAt;

  public static GlucoseData of(Long userId, Integer glucoseValue, LocalDateTime measuredAt) {
    GlucoseData glucoseData = new GlucoseData();
    glucoseData.userId = userId;
    glucoseData.glucoseValue = glucoseValue;
    glucoseData.measuredAt = measuredAt;
    return glucoseData;
  }
}
