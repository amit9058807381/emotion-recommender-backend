package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;

import java.util.List;

public interface RecommendationService {
    List<ContentItem> getRecommendations(Long userId, String emotion, String type);
    void recordSelection(Long userId, Long contentItemId, String emotion);
}