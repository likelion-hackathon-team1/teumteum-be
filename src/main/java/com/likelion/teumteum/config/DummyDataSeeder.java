package com.likelion.teumteum.config;

import com.likelion.teumteum.entity.health.GlucoseData;
import com.likelion.teumteum.entity.health.WimReport;
import com.likelion.teumteum.entity.health.enums.WimLevel;
import com.likelion.teumteum.entity.member.Member;
import com.likelion.teumteum.entity.member.PersonalizationProfile;
import com.likelion.teumteum.entity.member.enums.DataSource;
import com.likelion.teumteum.entity.member.enums.EatingResponseType;
import com.likelion.teumteum.entity.member.enums.PreferenceLevel;
import com.likelion.teumteum.repository.GlucoseDataRepository;
import com.likelion.teumteum.repository.MemberRepository;
import com.likelion.teumteum.repository.PersonalizationProfileRepository;
import com.likelion.teumteum.repository.WimReportRepository;
import com.likelion.teumteum.service.GlucoseBaselineService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DummyDataSeeder implements CommandLineRunner {

  private static final int SEED_DAYS = 7;
  private static final int INTERVAL_MINUTES = 5;
  private static final int MIN_GLUCOSE = 70;
  private static final int MAX_GLUCOSE = 140;

  private final MemberRepository memberRepository;
  private final GlucoseDataRepository glucoseDataRepository;
  private final GlucoseBaselineService glucoseBaselineService;
  private final WimReportRepository wimReportRepository;
  private final PersonalizationProfileRepository personalizationProfileRepository;

  @Override
  @Transactional
  public void run(String... args) {
    Member member = memberRepository.findAll().stream()
        .findFirst()
        .orElseGet(() -> memberRepository.save(
            Member.of(null, "테스트유저", "test@teumteum.com", true)));

    seedGlucoseData(member);
    seedWimReport(member);
    seedPersonalizationProfile(member);
  }

  private void seedGlucoseData(Member member) {
    if (glucoseDataRepository.count() > 0) {
      log.info("GlucoseData가 이미 존재하여 혈당 더미데이터 시딩을 건너뜁니다.");
      return;
    }

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime start = now.minusDays(SEED_DAYS);

    for (LocalDateTime measuredAt = start; measuredAt.isBefore(now); measuredAt = measuredAt.plusMinutes(INTERVAL_MINUTES)) {
      int glucoseValue = ThreadLocalRandom.current().nextInt(MIN_GLUCOSE, MAX_GLUCOSE + 1);
      glucoseDataRepository.save(GlucoseData.of(member.getId(), glucoseValue, measuredAt));
    }

    log.info("멤버 id={}에 대해 혈당 더미데이터 시딩 완료 (기간: {}일, 간격: {}분)", member.getId(), SEED_DAYS, INTERVAL_MINUTES);

    glucoseBaselineService.calculateBaseline(member.getId());
    log.info("멤버 id={} 초기 기준선 계산 완료 (실시간 시뮬레이션 시작 전 사전 계산)", member.getId());
  }

  private void seedWimReport(Member member) {
    if (wimReportRepository.existsByMemberId(member.getId())) {
      log.info("멤버 id={} WimReport가 이미 존재하여 시딩을 건너뜁니다.", member.getId());
      return;
    }

    WimReport report = WimReport.of(
        member.getId(), "WIM-TEST-0001", 82,
        WimLevel.B, WimLevel.B, WimLevel.B, WimLevel.A,
        180, 8500, 420, BigDecimal.valueOf(88.00),
        5, 3, 21,
        LocalDate.now(), LocalDateTime.now());

    wimReportRepository.save(report);
    log.info("멤버 id={} WimReport 시딩 완료", member.getId());
  }

  private void seedPersonalizationProfile(Member member) {
    if (personalizationProfileRepository.existsByMemberId(member.getId())) {
      log.info("멤버 id={} PersonalizationProfile이 이미 존재하여 시딩을 건너뜁니다.", member.getId());
      return;
    }

    PersonalizationProfile profile = PersonalizationProfile.of(
        member.getId(), DataSource.WIM_I,
        PreferenceLevel.MEDIUM, PreferenceLevel.MEDIUM,
        PreferenceLevel.MEDIUM, PreferenceLevel.HIGH,
        PreferenceLevel.MEDIUM, PreferenceLevel.MEDIUM,
        EatingResponseType.COMFORT_EATING);

    personalizationProfileRepository.save(profile);
    log.info("멤버 id={} PersonalizationProfile 시딩 완료", member.getId());
  }
}
