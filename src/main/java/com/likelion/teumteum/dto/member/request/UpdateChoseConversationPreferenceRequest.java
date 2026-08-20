package com.likelion.teumteum.dto.member.request;

import com.likelion.teumteum.entity.member.Member;

public record UpdateChoseConversationPreferenceRequest(
        Member.ConversationPreference conversationPreference
) {
}
