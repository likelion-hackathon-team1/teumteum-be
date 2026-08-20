package com.likelion.teumteum.controller;

import com.likelion.teumteum.common.dto.SuccessResponse;
import com.likelion.teumteum.dto.member.request.NotificationReceiptRequest;
import com.likelion.teumteum.dto.member.request.UpdateChoseConversationPreferenceRequest;
import com.likelion.teumteum.dto.member.response.CheckAllSettingsResponse;
import com.likelion.teumteum.service.member.OnboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members/{memberId}/onboarding")
@RequiredArgsConstructor
public class MemberController {

    private final OnboardingService onboardingService;

    @PatchMapping("/conversation")
    public ResponseEntity<SuccessResponse<?>> updateConversationPreference(
            @PathVariable Long memberId,
            @RequestBody UpdateChoseConversationPreferenceRequest request
    ) {
        onboardingService.choseConversationPreference(
                memberId,
                request
        );

        return ResponseEntity.ok(
                SuccessResponse.success("AI 대화 방식 선택 완료")
        );
    }

    @PatchMapping("/notification")
    public ResponseEntity<SuccessResponse<?>> updateNotificationEnabled(
            @PathVariable Long memberId,
            @RequestBody NotificationReceiptRequest request
    ) {
        onboardingService.updateNotificationEnabled(
                memberId,
                request
        );

        return ResponseEntity.ok(
                SuccessResponse.success("알림 수신 설정 완료")
        );
    }

    @GetMapping("/settings")
    public ResponseEntity<SuccessResponse<CheckAllSettingsResponse>> checkAllSettings(
            @PathVariable Long memberId
    ) {
        CheckAllSettingsResponse response = onboardingService.checkAllSettings(memberId);
        return ResponseEntity.ok(
                SuccessResponse.of(response, "온보딩 설정 데이터 확인 ")
        );
    }
}