package com.likelion.teumteum.repository;

import com.likelion.teumteum.entity.intervention.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

  List<Message> findByConversationIdOrderBySequenceAsc(Long conversationId);

  Optional<Message> findFirstByConversationIdOrderBySequenceDesc(Long conversationId);
}
