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

  @Column(nullable = false, columnDefinition = "TINYINT(1)")
  private boolean onboardingCompleted;

  public static Member of(Long kakaoUserId, String nickname, String email, boolean onboardingCompleted) {
    Member member = new Member();
    member.kakaoUserId = kakaoUserId;
    member.nickname = nickname;
    member.email = email;
    member.onboardingCompleted = onboardingCompleted;
    return member;
  }
}
