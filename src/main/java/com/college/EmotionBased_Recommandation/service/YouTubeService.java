package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import java.util.List;

public interface YouTubeService {
    List<ContentItem> searchContentForEmotion(String emotion, String type);
}