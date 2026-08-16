package com.college.EmotionBased_Recommandation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "emotion_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String emotion; // Happy, Sad, Angry, Neutral, Fear, Surprise

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "detected_at")
    private LocalDateTime detectedAt = LocalDateTime.now();
}