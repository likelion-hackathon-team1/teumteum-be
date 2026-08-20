package com.likelion.teumteum.service.member;

import com.likelion.teumteum.dto.member.request.NotificationReceiptRequest;
import com.likelion.teumteum.dto.member.request.UpdateChoseConversationPreferenceRequest;
import com.likelion.teumteum.dto.member.response.CheckAllSettingsResponse;
import com.likelion.teumteum.entity.member.Member;
import com.likelion.teumteum.exception.BusinessException;
import com.likelion.teumteum.exception.ErrorCode;
import com.likelion.teumteum.repository.MemberRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OnboardingService {

    private final MemberRepository memberRepository;

    //알림 수신 설정
    @Transactional
    public void updateNotificationEnabled(
            Long memberId,
            NotificationReceiptRequest request
    ) {
        Member member = findMemberById(memberId);
        member.updateNotificationEnabled(
                request.notificationEnabled()
        );
    }
    //AI 대화 방식 선택
    @Transactional
    public void choseConversationPreference(
            Long memberId,
            UpdateChoseConversationPreferenceRequest request
    ){
        Member member = findMemberById(memberId);
        member.updateConversationPreference(
                request.conversationPreference()
        );
    }

    @Transactional
    public CheckAllSettingsResponse checkAllSettings(
            Long memberId
    ){
        Member member = findMemberById(memberId);
        member.completeOnboarding();
        return CheckAllSettingsResponse.from(
                member.isNotificationEnabled(),
                member.getConversationPreference()
        );

    }

    //사용자 찾기
    private Member findMemberById(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
