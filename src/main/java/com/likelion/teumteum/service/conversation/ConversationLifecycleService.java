package com.likelion.teumteum.service.conversation;

import com.likelion.teumteum.entity.intervention.Conversation;
import com.likelion.teumteum.entity.intervention.enums.ConversationStatus;
import com.likelion.teumteum.repository.ConversationRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConversationLifecycleService {

  private static final Duration RESET_INTERVAL = Duration.ofHours(4);

  private final ConversationRepository conversationRepository;

  public Optional<Conversation> findValidActiveConversation(Long memberId) {
    Optional<Conversation> active = conversationRepository
        .findFirstByMemberIdAndStatusOrderByStartedAtDesc(memberId, ConversationStatus.ACTIVE);

    if (active.isEmpty()) {
      return Optional.empty();
    }

    Conversation conversation = active.get();
    if (conversation.isExpired(LocalDateTime.now(), RESET_INTERVAL)) {
      closeConversation(conversation);
      return Optional.empty();
    }
    return Optional.of(conversation);
  }

  private void closeConversation(Conversation conversation) {
    conversation.close(LocalDateTime.now());
    conversationRepository.save(conversation);
  }
}
