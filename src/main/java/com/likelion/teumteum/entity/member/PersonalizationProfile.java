package com.likelion.teumteum.entity.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "personalization_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalizationProfile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DataSource dataSource;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PreferenceLevel rewardPreference;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PreferenceLevel safetyPreference;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PreferenceLevel selfRegulationTendency;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PreferenceLevel socialOrientation;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PreferenceLevel evaluationSensitivity;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PreferenceLevel recognitionPreference;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EatingResponseType eatingResponseType;
}
