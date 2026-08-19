package com.likelion.teumteum.entity.intervention;

import com.likelion.teumteum.entity.intervention.enums.MessageSenderType;
import com.likelion.teumteum.entity.intervention.enums.MessageType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long conversationId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MessageSenderType senderType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MessageType messageType;

  @Lob
  private String content;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "JSON")
  private Map<String, Object> metadata;

  @Column(nullable = false)
  private Integer sequence;

  public static Message of(Long conversationId, MessageSenderType senderType, MessageType messageType,
      String content, Map<String, Object> metadata, Integer sequence) {
    Message message = new Message();
    message.conversationId = conversationId;
    message.senderType = senderType;
    message.messageType = messageType;
    message.content = content;
    message.metadata = metadata;
    message.sequence = sequence;
    return message;
  }
}
