package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import java.util.List;

public interface StoryService {
    List<ContentItem> generateStoriesForEmotion(String emotion);
}