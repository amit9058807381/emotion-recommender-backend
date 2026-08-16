package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import java.util.List;

public interface QuoteService {
    List<ContentItem> generateQuotesForEmotion(String emotion);
}