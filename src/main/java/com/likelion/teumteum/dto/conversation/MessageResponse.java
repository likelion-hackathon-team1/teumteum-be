package com.likelion.teumteum.dto.conversation;

import com.likelion.teumteum.entity.intervention.Message;

public record MessageResponse(
    Long id,
    String senderType,
    String messageType,
    String content,
    Integer sequence
) {

  public static MessageResponse from(Message message) {
    return new MessageResponse(
        message.getId(),
        message.getSenderType().name(),
        message.getMessageType().name(),
        message.getContent(),
        message.getSequence());
  }

  public static MessageResponse greeting(String content) {
    return new MessageResponse(null, "ASSISTANT", "TEXT", content, 1);
  }
}
