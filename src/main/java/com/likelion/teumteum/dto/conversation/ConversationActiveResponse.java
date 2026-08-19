package com.likelion.teumteum.dto.conversation;

import com.likelion.teumteum.entity.intervention.Conversation;
import com.likelion.teumteum.entity.intervention.Message;
import java.util.List;

public record ConversationActiveResponse(
    String type,
    Long conversationId,
    List<MessageResponse> messages
) {

  private static final String TYPE_GREETING = "GREETING";
  private static final String TYPE_CONVERSATION = "CONVERSATION";

  public static ConversationActiveResponse greeting(String greetingText) {
    return new ConversationActiveResponse(TYPE_GREETING, null, List.of(MessageResponse.greeting(greetingText)));
  }

  public static ConversationActiveResponse of(Conversation conversation, List<Message> messages) {
    return new ConversationActiveResponse(
        TYPE_CONVERSATION,
        conversation.getId(),
        messages.stream().map(MessageResponse::from).toList());
  }
}
