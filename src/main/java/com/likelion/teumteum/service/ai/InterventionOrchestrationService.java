package com.likelion.teumteum.service.ai;

import com.likelion.teumteum.entity.health.GlucoseAnomalyEvent;
import com.likelion.teumteum.entity.intervention.Conversation;
import com.likelion.teumteum.entity.intervention.Intervention;
import com.likelion.teumteum.entity.intervention.Message;
import com.likelion.teumteum.entity.intervention.enums.InterventionStatus;
import com.likelion.teumteum.entity.intervention.enums.MessageSenderType;
import com.likelion.teumteum.entity.intervention.enums.MessageType;
import com.likelion.teumteum.repository.ConversationRepository;
import com.likelion.teumteum.repository.InterventionRepository;
import com.likelion.teumteum.repository.MessageRepository;
import com.likelion.teumteum.service.conversation.ConversationLifecycleService;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterventionOrchestrationService {

  private static final int FIRST_MESSAGE_SEQUENCE = 1;

  private final InterventionRepository interventionRepository;
  private final ConversationRepository conversationRepository;
  private final MessageRepository messageRepository;
  private final InterventionContextAssembler interventionContextAssembler;
  private final InterventionAiService interventionAiService;
  private final ConversationLifecycleService conversationLifecycleService;

  public void handleAnomalyEvent(GlucoseAnomalyEvent anomalyEvent) {
    LocalDateTime now = LocalDateTime.now();
    Intervention intervention = interventionRepository.save(Intervention.of(
        anomalyEvent.getMemberId(), anomalyEvent.getId(), InterventionStatus.TRIGGERED, now, now));

    try {
      InterventionContext context = interventionContextAssembler.assemble(anomalyEvent.getId());
      String aiMessage = interventionAiService.generateInterventionMessage(context);

      Conversation conversation = conversationLifecycleService.findValidActiveConversation(intervention.getMemberId())
          .orElseGet(() -> conversationRepository.save(Conversation.ofSystemTriggered(
              intervention.getMemberId(), intervention.getId(), LocalDateTime.now())));

      int sequence = nextSequence(conversation.getId());
      messageRepository.save(Message.of(conversation.getId(), MessageSenderType.ASSISTANT,
          MessageType.ACTION_SUGGESTION, aiMessage, Map.of("interventionId", intervention.getId()), sequence));

      conversation.touch(LocalDateTime.now());
      conversationRepository.save(conversation);

      intervention.markInProgress();
      interventionRepository.save(intervention);

      log.info("Intervention id={} 개입 시작 완료 (conversationId={})", intervention.getId(), conversation.getId());
    } catch (Exception e) {
      log.error("Intervention id={} AI 개입 처리 실패", intervention.getId(), e);
      intervention.markFailed();
      interventionRepository.save(intervention);
    }
  }

  private int nextSequence(Long conversationId) {
    return messageRepository.findFirstByConversationIdOrderBySequenceDesc(conversationId)
        .map(message -> message.getSequence() + 1)
        .orElse(FIRST_MESSAGE_SEQUENCE);
  }
}
