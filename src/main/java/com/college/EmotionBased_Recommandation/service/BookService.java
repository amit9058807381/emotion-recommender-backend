package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import java.util.List;

public interface BookService {
    List<ContentItem> generateBooksForEmotion(String emotion);
}