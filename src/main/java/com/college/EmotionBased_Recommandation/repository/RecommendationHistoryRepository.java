package com.college.EmotionBased_Recommandation.repository;

import com.college.EmotionBased_Recommandation.entity.RecommendationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RecommendationHistoryRepository extends JpaRepository<RecommendationHistory, Long> {

    List<RecommendationHistory> findByUser_IdAndEmotion(Long userId, String emotion);

    Optional<RecommendationHistory> findByUser_IdAndContentItem_IdAndEmotion(
            Long userId, Long contentItemId, String emotion
    );

    List<RecommendationHistory> findByUser_IdAndWasSelectedTrueOrderByRecommendedAtDesc(Long userId);
}