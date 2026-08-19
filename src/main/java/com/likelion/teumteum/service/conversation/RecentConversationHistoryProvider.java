package com.likelion.teumteum.service.conversation;

import com.likelion.teumteum.entity.intervention.Conversation;
import com.likelion.teumteum.entity.intervention.Message;
import com.likelion.teumteum.repository.ConversationRepository;
import com.likelion.teumteum.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecentConversationHistoryProvider {

  private static final int RECENT_DAYS = 3;
  private static final String NO_HISTORY_MESSAGE = "최근 3일간 대화 이력 없음";

  private final ConversationRepository conversationRepository;
  private final MessageRepository messageRepository;

  public String buildRecentHistorySummary(Long memberId) {
    return buildRecentHistorySummary(memberId, null);
  }

  public String buildRecentHistorySummary(Long memberId, Long excludeConversationId) {
    LocalDateTime since = LocalDateTime.now().minusDays(RECENT_DAYS);
    List<Conversation> conversations = conversationRepository
        .findByMemberIdAndStartedAtAfterOrderByStartedAtAsc(memberId, since)
        .stream()
        .filter(conversation -> !conversation.getId().equals(excludeConversationId))
        .toList();

    if (conversations.isEmpty()) {
      return NO_HISTORY_MESSAGE;
    }

    StringBuilder builder = new StringBuilder();
    for (Conversation conversation : conversations) {
      List<Message> messages = messageRepository.findByConversationIdOrderBySequenceAsc(conversation.getId());
      for (Message message : messages) {
        builder.append("[%s] %s: %s%n".formatted(
            conversation.getStartedAt(), message.getSenderType(), message.getContent()));
      }
    }
    return builder.toString();
  }
}
