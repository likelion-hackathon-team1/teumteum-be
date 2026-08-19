package com.likelion.teumteum.config;

import com.likelion.teumteum.entity.health.GlucoseBaseline;
import com.likelion.teumteum.entity.member.Member;
import com.likelion.teumteum.repository.MemberRepository;
import com.likelion.teumteum.service.GlucoseBaselineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GlucoseBaselineScheduler {

  private final MemberRepository memberRepository;
  private final GlucoseBaselineService glucoseBaselineService;

  @Scheduled(cron = "0 15 0 * * *")
  public void calculateDailyBaselines() {
    for (Member member : memberRepository.findAll()) {
      try {
        GlucoseBaseline baseline = glucoseBaselineService.calculateBaseline(member.getId());
        if (baseline == null) {
          log.info("멤버 id={} 최근 3일 혈당 데이터가 없어 기준선 계산을 건너뜁니다.", member.getId());
        } else {
          log.info("멤버 id={} 기준선 계산 완료 (baselineValue={}, sampleCount={})",
              member.getId(), baseline.getBaselineValue(), baseline.getSampleCount());
        }
      } catch (Exception e) {
        log.error("멤버 id={} 기준선 계산 실패", member.getId(), e);
      }
    }
  }
}
