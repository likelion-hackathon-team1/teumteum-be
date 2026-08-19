package com.likelion.teumteum.repository;

import com.likelion.teumteum.entity.member.PersonalizationProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalizationProfileRepository extends JpaRepository<PersonalizationProfile, Long> {

  boolean existsByMemberId(Long memberId);

  Optional<PersonalizationProfile> findByMemberId(Long memberId);
}
