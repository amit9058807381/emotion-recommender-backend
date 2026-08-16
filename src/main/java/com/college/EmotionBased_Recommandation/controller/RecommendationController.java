package com.college.EmotionBased_Recommandation.controller;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import com.college.EmotionBased_Recommandation.helper.SelectionRequest;
import com.college.EmotionBased_Recommandation.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    // Get ranked recommendations for a user based on their detected emotion
    @GetMapping
    public List<ContentItem> getRecommendations(
            @RequestParam Long userId,
            @RequestParam String emotion,
            @RequestParam String type) {  // "music", "movie", or "video"

        return recommendationService.getRecommendations(userId, emotion, type);
    }

    // Called when user clicks/plays a recommended content item
    @PostMapping("/select")
    public void recordSelection(@RequestBody SelectionRequest request) {
        recommendationService.recordSelection(
                request.getUserId(),
                request.getContentItemId(),
                request.getEmotion()
        );
    }
}
