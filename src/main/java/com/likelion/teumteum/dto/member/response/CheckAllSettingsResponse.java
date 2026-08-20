package com.likelion.teumteum.dto.member.response;

import com.likelion.teumteum.entity.member.Member;

public record CheckAllSettingsResponse(
        boolean notificationEnabled,
        Member.ConversationPreference conversationPreference,
        //cgm 연동 여부
        boolean cgmConnected,
        boolean wimReportConnected,
        boolean wimIReportConnected
) {
    public static CheckAllSettingsResponse from(
            boolean notificationEnabled,
            Member.ConversationPreference conversationPreference
    ){
        return new CheckAllSettingsResponse(
        notificationEnabled,
        conversationPreference,
        true,
        true,
        true);
    }
}
