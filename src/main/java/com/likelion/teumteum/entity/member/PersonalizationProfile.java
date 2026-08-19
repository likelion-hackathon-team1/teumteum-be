package com.likelion.teumteum.entity.member;

import com.likelion.teumteum.entity.member.enums.DataSource;
import com.likelion.teumteum.entity.member.enums.EatingResponseType;
import com.likelion.teumteum.entity.member.enums.PreferenceLevel;
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

  public static PersonalizationProfile of(Long memberId, DataSource dataSource,
      PreferenceLevel rewardPreference, PreferenceLevel safetyPreference,
      PreferenceLevel selfRegulationTendency, PreferenceLevel socialOrientation,
      PreferenceLevel evaluationSensitivity, PreferenceLevel recognitionPreference,
      EatingResponseType eatingResponseType) {
    PersonalizationProfile profile = new PersonalizationProfile();
    profile.memberId = memberId;
    profile.dataSource = dataSource;
    profile.rewardPreference = rewardPreference;
    profile.safetyPreference = safetyPreference;
    profile.selfRegulationTendency = selfRegulationTendency;
    profile.socialOrientation = socialOrientation;
    profile.evaluationSensitivity = evaluationSensitivity;
    profile.recognitionPreference = recognitionPreference;
    profile.eatingResponseType = eatingResponseType;
    return profile;
  }
}
