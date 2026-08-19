package com.likelion.teumteum.entity.auth;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "refresh_token_id")
  private Long id;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "token", nullable = false)
  private String token;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;
}
