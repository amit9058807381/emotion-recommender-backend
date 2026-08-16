package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import java.util.List;

public interface ContentItemService {
    List<ContentItem> fetchAndCacheContentForEmotion(String emotion, String type);
    ContentItem getOrCacheExternalContent(String externalId, String sourceApi, ContentItem newItem);
}