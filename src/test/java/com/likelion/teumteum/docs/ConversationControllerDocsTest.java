package com.likelion.teumteum.docs;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.likelion.teumteum.controller.ConversationController;
import com.likelion.teumteum.dto.conversation.ConversationActiveResponse;
import com.likelion.teumteum.dto.conversation.MessageResponse;
import com.likelion.teumteum.dto.conversation.SendMessageRequest;
import com.likelion.teumteum.service.conversation.ConversationService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

public class ConversationControllerDocsTest extends RestDocsSupport {

  private final ConversationService conversationService = Mockito.mock(ConversationService.class);

  @Override
  protected Object initController() {
    return new ConversationController(conversationService);
  }

  @Test
  @DisplayName("활성 대화 조회 - 신규 대화(인사말)")
  void getActiveConversation_greeting() throws Exception {
    ConversationActiveResponse response = ConversationActiveResponse.greeting("안녕하세요! 오늘 컨디션은 어떠세요?");

    given(conversationService.getActiveConversation(1L)).willReturn(response);

    mockMvc.perform(get("/api/members/{memberId}/conversations/active", 1L))
        .andExpect(status().isOk())
        .andDo(document("conversation-get-active-greeting",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("memberId").description("회원 ID")
            ),
            responseFields(
                fieldWithPath("data.type").description("응답 타입 (GREETING, CONVERSATION)"),
                fieldWithPath("data.conversationId").description("대화 ID (신규 인사말인 경우 null)").optional(),
                fieldWithPath("data.messages").description("메시지 목록"),
                fieldWithPath("data.messages[].id").description("메시지 ID (신규 인사말인 경우 null)").optional(),
                fieldWithPath("data.messages[].senderType").description("발신자 타입 (USER, ASSISTANT)"),
                fieldWithPath("data.messages[].messageType").description("메시지 타입 (TEXT 등)"),
                fieldWithPath("data.messages[].content").description("메시지 내용"),
                fieldWithPath("data.messages[].sequence").description("메시지 순번"),
                fieldWithPath("message").description("응답 메시지")
            )
        ));
  }

  @Test
  @DisplayName("활성 대화 조회 - 진행 중인 대화")
  void getActiveConversation_ongoing() throws Exception {
    MessageResponse userMessage = new MessageResponse(1L, "USER", "TEXT", "오늘 혈당이 높게 나왔어요", 1);
    MessageResponse assistantMessage = new MessageResponse(2L, "ASSISTANT", "TEXT", "어떤 부분이 걱정되시나요?", 2);
    ConversationActiveResponse response = new ConversationActiveResponse(
        "CONVERSATION", 10L, List.of(userMessage, assistantMessage));

    given(conversationService.getActiveConversation(1L)).willReturn(response);

    mockMvc.perform(get("/api/members/{memberId}/conversations/active", 1L))
        .andExpect(status().isOk())
        .andDo(document("conversation-get-active",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("memberId").description("회원 ID")
            ),
            responseFields(
                fieldWithPath("data.type").description("응답 타입 (GREETING, CONVERSATION)"),
                fieldWithPath("data.conversationId").description("대화 ID"),
                fieldWithPath("data.messages").description("메시지 목록"),
                fieldWithPath("data.messages[].id").description("메시지 ID"),
                fieldWithPath("data.messages[].senderType").description("발신자 타입 (USER, ASSISTANT)"),
                fieldWithPath("data.messages[].messageType").description("메시지 타입 (TEXT 등)"),
                fieldWithPath("data.messages[].content").description("메시지 내용"),
                fieldWithPath("data.messages[].sequence").description("메시지 순번"),
                fieldWithPath("message").description("응답 메시지")
            )
        ));
  }

  @Test
  @DisplayName("사용자 메시지 전송")
  void sendMessage() throws Exception {
    SendMessageRequest request = new SendMessageRequest("오늘 혈당이 높게 나왔어요");

    MessageResponse userMessage = new MessageResponse(1L, "USER", "TEXT", "오늘 혈당이 높게 나왔어요", 1);
    MessageResponse assistantMessage = new MessageResponse(2L, "ASSISTANT", "TEXT", "어떤 부분이 걱정되시나요?", 2);
    ConversationActiveResponse response = new ConversationActiveResponse(
        "CONVERSATION", 10L, List.of(userMessage, assistantMessage));

    given(conversationService.sendUserMessage(1L, request.content())).willReturn(response);

    mockMvc.perform(post("/api/members/{memberId}/conversations/messages", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(document("conversation-send-message",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("memberId").description("회원 ID")
            ),
            requestFields(
                fieldWithPath("content").description("사용자가 보낸 메시지 내용")
            ),
            responseFields(
                fieldWithPath("data.type").description("응답 타입 (GREETING, CONVERSATION)"),
                fieldWithPath("data.conversationId").description("대화 ID"),
                fieldWithPath("data.messages").description("메시지 목록"),
                fieldWithPath("data.messages[].id").description("메시지 ID"),
                fieldWithPath("data.messages[].senderType").description("발신자 타입 (USER, ASSISTANT)"),
                fieldWithPath("data.messages[].messageType").description("메시지 타입 (TEXT 등)"),
                fieldWithPath("data.messages[].content").description("메시지 내용"),
                fieldWithPath("data.messages[].sequence").description("메시지 순번"),
                fieldWithPath("message").description("응답 메시지")
            )
        ));
  }
}
