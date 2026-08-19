package com.likelion.teumteum.service.conversation;

import com.likelion.teumteum.dto.conversation.ConversationActiveResponse;
import com.likelion.teumteum.entity.intervention.Conversation;
import com.likelion.teumteum.entity.intervention.Message;
import com.likelion.teumteum.entity.intervention.enums.MessageSenderType;
import com.likelion.teumteum.entity.intervention.enums.MessageType;
import com.likelion.teumteum.entity.member.Member;
import com.likelion.teumteum.repository.ConversationRepository;
import com.likelion.teumteum.repository.MemberRepository;
import com.likelion.teumteum.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationService {

  private static final int FIRST_MESSAGE_SEQUENCE = 1;

  private final MemberRepository memberRepository;
  private final ConversationRepository conversationRepository;
  private final MessageRepository messageRepository;
  private final GreetingService greetingService;
  private final ConversationAiService conversationAiService;
  private final ConversationLifecycleService conversationLifecycleService;
  private final RecentConversationHistoryProvider recentConversationHistoryProvider;

  public ConversationActiveResponse getActiveConversation(Long memberId) {
    Member member = findMember(memberId);

    return conversationLifecycleService.findValidActiveConversation(memberId)
        .map(conversation -> {
          List<Message> messages = messageRepository.findByConversationIdOrderBySequenceAsc(conversation.getId());
          return ConversationActiveResponse.of(conversation, messages);
        })
        .orElseGet(() -> ConversationActiveResponse.greeting(greetingService.generateGreeting(member.getNickname())));
  }

  public ConversationActiveResponse sendUserMessage(Long memberId, String content) {
    findMember(memberId);

    Conversation conversation = conversationLifecycleService.findValidActiveConversation(memberId)
        .orElseGet(() -> conversationRepository.save(Conversation.ofUserInitiated(memberId, LocalDateTime.now())));

    int userMessageSequence = nextSequence(conversation.getId());
    messageRepository.save(Message.of(conversation.getId(), MessageSenderType.USER, MessageType.TEXT,
        content, null, userMessageSequence));

    List<Message> history = messageRepository.findByConversationIdOrderBySequenceAsc(conversation.getId());
    String recentHistorySummary = recentConversationHistoryProvider
        .buildRecentHistorySummary(memberId, conversation.getId());
    String aiReply = conversationAiService.generateReply(recentHistorySummary, history);

    messageRepository.save(Message.of(conversation.getId(), MessageSenderType.ASSISTANT, MessageType.TEXT,
        aiReply, null, userMessageSequence + 1));

    LocalDateTime now = LocalDateTime.now();
    conversation.touch(now);
    conversationRepository.save(conversation);

    List<Message> updatedMessages = messageRepository.findByConversationIdOrderBySequenceAsc(conversation.getId());
    log.info("Conversation id={} 사용자 메시지 처리 완료", conversation.getId());
    return ConversationActiveResponse.of(conversation, updatedMessages);
  }

  private int nextSequence(Long conversationId) {
    return messageRepository.findFirstByConversationIdOrderBySequenceDesc(conversationId)
        .map(message -> message.getSequence() + 1)
        .orElse(FIRST_MESSAGE_SEQUENCE);
  }

  private Member findMember(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Member id=" + memberId));
  }
}
