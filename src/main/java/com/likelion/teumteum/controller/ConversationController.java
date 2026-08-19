package com.likelion.teumteum.controller;

import com.likelion.teumteum.dto.conversation.ConversationActiveResponse;
import com.likelion.teumteum.dto.conversation.SendMessageRequest;
import com.likelion.teumteum.service.conversation.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members/{memberId}/conversations")
@RequiredArgsConstructor
public class ConversationController {

  private final ConversationService conversationService;

  @GetMapping("/active")
  public ConversationActiveResponse getActiveConversation(@PathVariable Long memberId) {
    return conversationService.getActiveConversation(memberId);
  }

  @PostMapping("/messages")
  public ConversationActiveResponse sendMessage(@PathVariable Long memberId,
      @RequestBody SendMessageRequest request) {
    return conversationService.sendUserMessage(memberId, request.content());
  }
}
