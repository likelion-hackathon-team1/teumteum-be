package com.likelion.teumteum.entity.intervention;

import com.likelion.teumteum.entity.intervention.enums.ConversationStatus;
import com.likelion.teumteum.entity.intervention.enums.ConversationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Conversation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long memberId;

  private Long interventionId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ConversationType conversationType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ConversationStatus status;

  @Column(nullable = false)
  private LocalDateTime startedAt;

  @Column(nullable = false)
  private LocalDateTime lastActivityAt;

  private LocalDateTime endedAt;

  public static Conversation ofSystemTriggered(Long memberId, Long interventionId, LocalDateTime startedAt) {
    Conversation conversation = new Conversation();
    conversation.memberId = memberId;
    conversation.interventionId = interventionId;
    conversation.conversationType = ConversationType.SYSTEM_TRIGGERED;
    conversation.status = ConversationStatus.ACTIVE;
    conversation.startedAt = startedAt;
    conversation.lastActivityAt = startedAt;
    return conversation;
  }

  public static Conversation ofUserInitiated(Long memberId, LocalDateTime startedAt) {
    Conversation conversation = new Conversation();
    conversation.memberId = memberId;
    conversation.interventionId = null;
    conversation.conversationType = ConversationType.USER_INITIATED;
    conversation.status = ConversationStatus.ACTIVE;
    conversation.startedAt = startedAt;
    conversation.lastActivityAt = startedAt;
    return conversation;
  }

  public void touch(LocalDateTime activityAt) {
    this.lastActivityAt = activityAt;
  }

  public boolean isExpired(LocalDateTime now, Duration resetInterval) {
    return lastActivityAt.plus(resetInterval).isBefore(now);
  }

  public void complete(LocalDateTime endedAt) {
    this.status = ConversationStatus.COMPLETED;
    this.endedAt = endedAt;
  }

  public void close(LocalDateTime endedAt) {
    this.status = ConversationStatus.CLOSED;
    this.endedAt = endedAt;
  }
}
