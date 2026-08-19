package com.likelion.teumteum.service.conversation;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class GreetingService {

  private static final List<String> MORNING_GREETINGS = List.of(
      "%s님, 좋은 아침이에요! 오늘 하루도 컨디션 잘 챙겨봐요.",
      "%s님, 상쾌한 아침이네요! 가벼운 스트레칭으로 하루를 시작해볼까요?",
      "%s님, 안녕하세요! 오늘도 건강한 하루 함께 만들어봐요."
  );

  private static final List<String> AFTERNOON_GREETINGS = List.of(
      "%s님, 오후에도 활기차게 보내고 계신가요?",
      "%s님, 점심 식사 후 컨디션은 괜찮으세요?",
      "%s님, 안녕하세요! 잠깐 스트레칭 한 번 어때요?"
  );

  private static final List<String> EVENING_GREETINGS = List.of(
      "%s님, 오늘 하루도 고생 많으셨어요.",
      "%s님, 저녁 시간이네요. 오늘 컨디션은 어떠셨어요?",
      "%s님, 안녕하세요! 오늘 하루를 잘 마무리해봐요."
  );

  private static final List<String> NIGHT_GREETINGS = List.of(
      "%s님, 늦은 시간까지 고생이 많으세요.",
      "%s님, 편안한 밤 되고 계신가요?",
      "%s님, 안녕하세요! 오늘 하루를 되돌아보는 시간은 어떠세요?"
  );

  private final Random random = new Random();

  public String generateGreeting(String nickname) {
    List<String> pool = pickPoolByTime(LocalTime.now());
    String template = pool.get(random.nextInt(pool.size()));
    return template.formatted(nickname);
  }

  private List<String> pickPoolByTime(LocalTime now) {
    int hour = now.getHour();
    if (hour >= 5 && hour < 12) {
      return MORNING_GREETINGS;
    }
    if (hour >= 12 && hour < 18) {
      return AFTERNOON_GREETINGS;
    }
    if (hour >= 18 && hour < 22) {
      return EVENING_GREETINGS;
    }
    return NIGHT_GREETINGS;
  }
}
