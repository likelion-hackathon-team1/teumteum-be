package com.likelion.teumteum.repository;

import com.likelion.teumteum.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
