package com.likelion.teumteum.service.conversation;

import com.likelion.teumteum.entity.intervention.Message;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConversationAiService {

  private static final String SYSTEM_PROMPT = """
      너는 사용자의 혈당 및 생활습관 관리를 돕는 헬스케어 코치야.
      아래 이전 대화 맥락을 참고해서 사용자의 질문이나 고민에 공감하며,
      비난하지 않는 어조로 실천 가능한 조언을 2~3문장의 한국어로 답해.
      """;

  private final ChatClient chatClient;

  public ConversationAiService(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  public String generateReply(String recentHistorySummary, List<Message> history) {
    List<org.springframework.ai.chat.messages.Message> aiMessages = history.stream()
        .map(this::toAiMessage)
        .toList();

    String systemPrompt = SYSTEM_PROMPT + "\n\n[최근 3일간 대화 요약]\n" + recentHistorySummary;

    String response = chatClient.prompt()
        .system(systemPrompt)
        .messages(aiMessages)
        .call()
        .content();

    log.info("AI 대화 응답 생성 완료 (historySize={})", history.size());
    return response;
  }

  private org.springframework.ai.chat.messages.Message toAiMessage(Message message) {
    return switch (message.getSenderType()) {
      case USER -> new UserMessage(message.getContent());
      case ASSISTANT -> new AssistantMessage(message.getContent());
      case SYSTEM -> new SystemMessage(message.getContent());
    };
  }
}
