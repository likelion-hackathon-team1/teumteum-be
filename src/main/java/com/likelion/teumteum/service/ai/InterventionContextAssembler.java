package com.likelion.teumteum.service.ai;

import com.likelion.teumteum.entity.health.GlucoseAnomalyEvent;
import com.likelion.teumteum.entity.health.WimReport;
import com.likelion.teumteum.entity.member.PersonalizationProfile;
import com.likelion.teumteum.repository.GlucoseAnomalyEventRepository;
import com.likelion.teumteum.repository.PersonalizationProfileRepository;
import com.likelion.teumteum.repository.WimReportRepository;
import com.likelion.teumteum.service.conversation.RecentConversationHistoryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InterventionContextAssembler {

  private final GlucoseAnomalyEventRepository glucoseAnomalyEventRepository;
  private final WimReportRepository wimReportRepository;
  private final PersonalizationProfileRepository personalizationProfileRepository;
  private final RecentConversationHistoryProvider recentConversationHistoryProvider;

  public InterventionContext assemble(Long anomalyEventId) {
    GlucoseAnomalyEvent anomalyEvent = glucoseAnomalyEventRepository.findById(anomalyEventId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 GlucoseAnomalyEvent id=" + anomalyEventId));

    WimReport wimReport = wimReportRepository
        .findFirstByMemberIdOrderByMeasuredAtDesc(anomalyEvent.getMemberId())
        .orElse(null);

    PersonalizationProfile personalizationProfile = personalizationProfileRepository
        .findByMemberId(anomalyEvent.getMemberId())
        .orElse(null);

    String recentConversationHistory = recentConversationHistoryProvider
        .buildRecentHistorySummary(anomalyEvent.getMemberId());

    return new InterventionContext(anomalyEvent, wimReport, personalizationProfile, recentConversationHistory);
  }
}
