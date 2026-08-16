package com.college.EmotionBased_Recommandation.repository;

import com.college.EmotionBased_Recommandation.entity.RecommendationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RecommendationHistoryRepository extends JpaRepository<RecommendationHistory, Long> {

    List<RecommendationHistory> findByUser_IdAndEmotionLog_Emotion(Long userId, String emotion);

    Optional<RecommendationHistory> findByUser_IdAndContentItem_IdAndEmotionLog_Emotion(
            Long userId, Long contentItemId, String emotion
    );
}
