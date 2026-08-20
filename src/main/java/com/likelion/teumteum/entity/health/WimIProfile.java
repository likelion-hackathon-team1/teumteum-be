package com.likelion.teumteum.entity.health;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wim_i_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WimIProfile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long memberId;

  @Column(nullable = false)
  private Integer boredomAvoidanceScore;

  @Column(nullable = false)
  private Integer impulsivityScore;

  @Column(nullable = false)
  private Integer disinhibitionScore;

  @Column(nullable = false)
  private Integer anxietySensitivityScore;

  @Column(nullable = false)
  private Integer harmAvoidanceScore;

  @Column(nullable = false)
  private Integer fearOfUncertaintyScore;

  @Column(nullable = false)
  private Integer interpersonalSensitivityScore;

  @Column(nullable = false)
  private Integer emotionalOpennessScore;

  @Column(nullable = false)
  private Integer dependenceScore;

  @Column(nullable = false)
  private Integer recognitionSeekingScore;

  @Column(nullable = false)
  private LocalDate measuredAt;

  public static WimIProfile of(Long memberId,
      Integer boredomAvoidanceScore, Integer impulsivityScore, Integer disinhibitionScore,
      Integer anxietySensitivityScore, Integer harmAvoidanceScore, Integer fearOfUncertaintyScore,
      Integer interpersonalSensitivityScore, Integer emotionalOpennessScore,
      Integer dependenceScore, Integer recognitionSeekingScore, LocalDate measuredAt) {
    WimIProfile profile = new WimIProfile();
    profile.memberId = memberId;
    profile.boredomAvoidanceScore = boredomAvoidanceScore;
    profile.impulsivityScore = impulsivityScore;
    profile.disinhibitionScore = disinhibitionScore;
    profile.anxietySensitivityScore = anxietySensitivityScore;
    profile.harmAvoidanceScore = harmAvoidanceScore;
    profile.fearOfUncertaintyScore = fearOfUncertaintyScore;
    profile.interpersonalSensitivityScore = interpersonalSensitivityScore;
    profile.emotionalOpennessScore = emotionalOpennessScore;
    profile.dependenceScore = dependenceScore;
    profile.recognitionSeekingScore = recognitionSeekingScore;
    profile.measuredAt = measuredAt;
    return profile;
  }
}
