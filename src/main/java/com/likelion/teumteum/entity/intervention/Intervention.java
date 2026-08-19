package com.likelion.teumteum.entity.intervention;

import com.likelion.teumteum.entity.intervention.enums.InterventionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "intervention")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Intervention {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id", nullable = false)
  private Long memberId;

  @Column(name = "anomaly_event_id", nullable = false)
  private Long anomalyEventId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private InterventionStatus status;

  @Column(nullable = false)
  private LocalDateTime triggeredAt;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  public static Intervention of(Long memberId, Long anomalyEventId, InterventionStatus status,
      LocalDateTime triggeredAt, LocalDateTime createdAt) {
    Intervention intervention = new Intervention();
    intervention.memberId = memberId;
    intervention.anomalyEventId = anomalyEventId;
    intervention.status = status;
    intervention.triggeredAt = triggeredAt;
    intervention.createdAt = createdAt;
    return intervention;
  }

  public void markInProgress() {
    this.status = InterventionStatus.IN_PROGRESS;
  }

  public void markCompleted() {
    this.status = InterventionStatus.COMPLETED;
  }

  public void markClosed() {
    this.status = InterventionStatus.CLOSED;
  }

  public void markFailed() {
    this.status = InterventionStatus.FAILED;
  }
}
