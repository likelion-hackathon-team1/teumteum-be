package com.likelion.teumteum.entity.health;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cgm_connection")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CgmConnection {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ConnectionStatus connectionStatus;

  @Column(nullable = false)
  private boolean deviceProvided;

  private LocalDateTime connectedAt;

  private LocalDateTime lastSyncedAt;

  private LocalDateTime lastDataUpdatedAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SyncStatus syncStatus;
}
