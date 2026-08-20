package com.likelion.teumteum.entity.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "kakao_user_id")
  private Long kakaoUserId;

  @Column(nullable = false)
  private String nickname;

  private String email;

  @Column(name = "profile_image_url")
  private String profileImageUrl;

  @Column(
          name = "onboarding_completed",
          nullable = false,
          columnDefinition = "TINYINT(1)"
  )
  private boolean onboardingCompleted;

  @Enumerated(EnumType.STRING)
  @Column(
          name = "conversation_preference",
          nullable = false
  )
  private ConversationPreference conversationPreference =
          ConversationPreference.ASK_FIRST;

  @Column(
          name = "notification_enabled",
          nullable = false,
          columnDefinition = "TINYINT(1)"
  )
  private boolean notificationEnabled;

  @Column(
          name = "cgm_connected",
          nullable = false,
          columnDefinition = "TINYINT(1)"
  )
  private boolean cgmConnected = true;

  @Column(
          name = "wim_report_connected",
          nullable = false,
          columnDefinition = "TINYINT(1)"
  )
  private boolean wimReportConnected = true;

  @Column(
          name = "wimi_report_connected",
          nullable = false,
          columnDefinition = "TINYINT(1)"
  )
  private boolean wimIReportConnected = true;

  public static Member of(
          Long kakaoUserId,
          String nickname,
          String email,
          String profileImageUrl
  ) {
    Member member = new Member();
    member.kakaoUserId = kakaoUserId;
    member.nickname = nickname;
    member.email = email;
    member.profileImageUrl = profileImageUrl;
    member.onboardingCompleted = false;
    member.notificationEnabled = false;

    return member;
  }

  // 온보딩 - 대화 방식 선택/변경
  public void updateConversationPreference(
          ConversationPreference conversationPreference
  ) {
    this.conversationPreference = conversationPreference;
  }

  // 온보딩 - 알림 수신 설정
  public void updateNotificationEnabled(boolean notificationEnabled) {
    this.notificationEnabled = notificationEnabled;
  }

  // 온보딩 완료
  public void completeOnboarding() {
    this.onboardingCompleted = true;
  }

  public enum ConversationPreference {

    // 행동 → 짧은 이유 → 선택 확인
    ACTION_FIRST,

    // 짧은 이유 → 행동 → 선택 확인
    REASON_FIRST,

    // 질문 → 사용자 응답 → 공감/확인 → 행동
    ASK_FIRST,

    // 경청 → 공감 → 제안 의사 확인 → 행동
    LISTEN_FIRST
  }
}