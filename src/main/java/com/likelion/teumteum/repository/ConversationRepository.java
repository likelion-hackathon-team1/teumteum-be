package com.likelion.teumteum.repository;

import com.likelion.teumteum.entity.intervention.Conversation;
import com.likelion.teumteum.entity.intervention.enums.ConversationStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

  Optional<Conversation> findFirstByMemberIdAndStatusOrderByStartedAtDesc(Long memberId, ConversationStatus status);

  List<Conversation> findByMemberIdAndStartedAtAfterOrderByStartedAtAsc(Long memberId, LocalDateTime since);
}
