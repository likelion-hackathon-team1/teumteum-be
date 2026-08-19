package com.likelion.teumteum.entity.health;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wim_grade_definition")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WimGradeDefinition {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private WimGradeCategory category;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private WimGrade grade;

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String description;

  @Column(columnDefinition = "TEXT")
  private String aiGuidance;
}
