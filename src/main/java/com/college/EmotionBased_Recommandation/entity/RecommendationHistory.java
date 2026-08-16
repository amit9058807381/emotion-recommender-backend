package com.college.EmotionBased_Recommandation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private ContentItem contentItem;

    @ManyToOne
    @JoinColumn(name = "emotion_log_id")
    private EmotionLog emotionLog;

    @Column(name = "recommended_at")
    private LocalDateTime recommendedAt = LocalDateTime.now();

    @Column(name = "was_selected")
    private Boolean wasSelected = false; // true = user clicked/played this content

    @Column(name = "selection_count")
    private Integer selectionCount = 0; // how many times user engaged — used for top-ranking
}