package com.likelion.teumteum.service.ai;

import com.likelion.teumteum.entity.health.GlucoseAnomalyEvent;
import com.likelion.teumteum.entity.health.WimReport;
import com.likelion.teumteum.entity.member.PersonalizationProfile;

public record InterventionContext(
    GlucoseAnomalyEvent anomalyEvent,
    WimReport wimReport,
    PersonalizationProfile personalizationProfile,
    String recentConversationHistory
) {
}
