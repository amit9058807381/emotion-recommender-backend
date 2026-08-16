package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import com.college.EmotionBased_Recommandation.entity.RecommendationHistory;
import com.college.EmotionBased_Recommandation.entity.User;
import com.college.EmotionBased_Recommandation.repository.ContentItemRepository;
import com.college.EmotionBased_Recommandation.repository.RecommendationHistoryRepository;
import com.college.EmotionBased_Recommandation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private ContentItemRepository contentItemRepository;

    @Autowired
    private ContentItemService contentItemService;

    @Autowired
    private RecommendationHistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ContentItem> getRecommendations(Long userId, String emotion, String type) {
        List<ContentItem> matchingContent = contentItemService.fetchAndCacheContentForEmotion(emotion, type);

        Map<Long, Integer> engagementMap = historyRepository
                .findByUser_IdAndEmotionLog_Emotion(userId, emotion)
                .stream()
                .filter(RecommendationHistory::getWasSelected)
                .collect(Collectors.toMap(
                        h -> h.getContentItem().getId(),
                        RecommendationHistory::getSelectionCount,
                        (a, b) -> a
                ));

        return matchingContent.stream()
                .sorted((a, b) -> {
                    int countA = engagementMap.getOrDefault(a.getId(), 0);
                    int countB = engagementMap.getOrDefault(b.getId(), 0);
                    return Integer.compare(countB, countA);
                })
                .collect(Collectors.toList());
    }
    @Override
    public void recordSelection(Long userId, Long contentItemId, String emotion) {
        Optional<RecommendationHistory> existing = historyRepository
                .findByUser_IdAndContentItem_IdAndEmotionLog_Emotion(userId, contentItemId, emotion);

        if (existing.isPresent()) {
            RecommendationHistory history = existing.get();
            history.setSelectionCount(history.getSelectionCount() + 1);
            history.setWasSelected(true);
            historyRepository.save(history);
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            ContentItem contentItem = contentItemRepository.findById(contentItemId)
                    .orElseThrow(() -> new RuntimeException("Content not found: " + contentItemId));

            RecommendationHistory history = new RecommendationHistory();
            history.setUser(user);
            history.setContentItem(contentItem);
            history.setWasSelected(true);
            history.setSelectionCount(1);
            history.setRecommendedAt(LocalDateTime.now());
            historyRepository.save(history);
        }
    }
}