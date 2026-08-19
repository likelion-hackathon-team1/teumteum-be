package com.likelion.teumteum.service.ai;

import com.likelion.teumteum.entity.health.GlucoseAnomalyEvent;
import com.likelion.teumteum.entity.health.WimReport;
import com.likelion.teumteum.entity.member.PersonalizationProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InterventionAiService {

  private static final String SYSTEM_PROMPT = """
      너는 당뇨 전조증상(혈당 이상치)이 감지된 사용자에게 짧고 공감가는 개입 메시지를 보내는 헬스케어 코치야.
      아래로 제공되는 사용자의 혈당 이상치 정보, WIM 검사 결과, 개인화 성향 프로필을 참고해서
      비난하지 않는 어조로, 실천 가능한 행동 하나를 제안하는 2~3문장의 한국어 메시지를 작성해.
      """;

  private final ChatClient chatClient;

  public InterventionAiService(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  public String generateInterventionMessage(InterventionContext context) {
    String userPrompt = buildUserPrompt(context);

    String response = chatClient.prompt()
        .system(SYSTEM_PROMPT)
        .user(userPrompt)
        .call()
        .content();

    log.info("AI 개입 메시지 생성 완료 (anomalyEventId={})", context.anomalyEvent().getId());
    return response;
  }

  private String buildUserPrompt(InterventionContext context) {
    StringBuilder prompt = new StringBuilder();
    prompt.append(describeAnomalyEvent(context.anomalyEvent()));
    prompt.append(describeWimReport(context.wimReport()));
    prompt.append(describePersonalizationProfile(context.personalizationProfile()));
    prompt.append(describeRecentConversationHistory(context.recentConversationHistory()));
    return prompt.toString();
  }

  private String describeRecentConversationHistory(String recentConversationHistory) {
    return """
        [최근 3일간 대화 이력]
        %s
        """.formatted(recentConversationHistory);
  }

  private String describeAnomalyEvent(GlucoseAnomalyEvent event) {
    return """
        [혈당 이상치 정보]
        - 심각도: %s
        - 상승 속도: %s
        - 지속 유형: %s
        - 발생 상황: %s
        - 시간대: %s
        - 요일 상황: %s
        - 감지 시각: %s
        """.formatted(
        event.getSeverity(), event.getRiseSpeed(), event.getDurationType(),
        event.getOccurrenceContext(), event.getTimeContext(), event.getDayContext(),
        event.getDetectedAt());
  }

  private String describeWimReport(WimReport wimReport) {
    if (wimReport == null) {
      return "[WIM 검사 결과] 없음\n";
    }
    return """
        [WIM 검사 결과]
        - 종합 점수: %s
        - 운동 등급: %s, 수면 등급: %s, 일상활동 등급: %s, 정신건강 등급: %s
        - 주간 운동시간: %s분, 일평균 걸음수: %s보
        - 수면시간: %s분, 수면효율: %s%%
        - 불안 점수: %s, 우울 점수: %s, 스트레스 점수: %s
        """.formatted(
        wimReport.getWimScore(), wimReport.getExerciseLevel(), wimReport.getSleepLevel(),
        wimReport.getDailyLifeLevel(), wimReport.getMentalHealthLevel(),
        wimReport.getWeeklyExerciseMinutes(), wimReport.getAverageDailySteps(),
        wimReport.getSleepMinutes(), wimReport.getSleepEfficiency(),
        wimReport.getAnxietyScore(), wimReport.getDepressionScore(), wimReport.getStressScore());
  }

  private String describePersonalizationProfile(PersonalizationProfile profile) {
    if (profile == null) {
      return "[개인화 성향 프로필] 없음\n";
    }
    return """
        [개인화 성향 프로필]
        - 즉각적 보상 선호: %s
        - 안전 추구 성향: %s
        - 자기조절 성향: %s
        - 사회지향성: %s
        - 평가 민감도: %s
        - 인정 선호: %s
        - 식이 반응 유형: %s
        """.formatted(
        profile.getRewardPreference(), profile.getSafetyPreference(),
        profile.getSelfRegulationTendency(), profile.getSocialOrientation(),
        profile.getEvaluationSensitivity(), profile.getRecognitionPreference(),
        profile.getEatingResponseType());
  }
}
