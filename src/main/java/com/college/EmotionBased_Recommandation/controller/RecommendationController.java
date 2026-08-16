package com.college.EmotionBased_Recommandation.controller;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import com.college.EmotionBased_Recommandation.helper.HistoryItemResponse;
import com.college.EmotionBased_Recommandation.helper.SelectionRequest;
import com.college.EmotionBased_Recommandation.repository.RecommendationHistoryRepository;
import com.college.EmotionBased_Recommandation.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private RecommendationHistoryRepository recommendationHistoryRepository;

    @GetMapping
    public List<ContentItem> getRecommendations(
            @RequestParam Long userId,
            @RequestParam String emotion,
            @RequestParam String type) {

        return recommendationService.getRecommendations(userId, emotion, type);
    }

    @PostMapping("/select")
    public void recordSelection(@RequestBody SelectionRequest request) {
        recommendationService.recordSelection(
                request.getUserId(),
                request.getContentItemId(),
                request.getEmotion()
        );
    }

    @GetMapping("/history/{userId}")
    public List<HistoryItemResponse> getHistory(@PathVariable Long userId) {
        return recommendationHistoryRepository
                .findByUser_IdAndWasSelectedTrueOrderByRecommendedAtDesc(userId)
                .stream()
                .map(h -> new HistoryItemResponse(
                        h.getContentItem().getId(),
                        h.getContentItem().getTitle(),
                        h.getContentItem().getType(),
                        h.getContentItem().getUrl(),
                        h.getContentItem().getThumbnailUrl(),
                        h.getEmotion(),
                        h.getRecommendedAt()
                ))
                .collect(Collectors.toList());
    }
}