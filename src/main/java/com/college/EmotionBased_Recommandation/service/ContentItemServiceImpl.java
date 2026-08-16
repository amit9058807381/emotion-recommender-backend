package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import com.college.EmotionBased_Recommandation.repository.ContentItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ContentItemServiceImpl implements ContentItemService {

    @Autowired
    private ContentItemRepository contentItemRepository;

    @Autowired
    private YouTubeService youTubeService;

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private StoryService storyService;

    @Autowired
    private BookService bookService;

    @Override
    public List<ContentItem> fetchAndCacheContentForEmotion(String emotion, String type) {
        List<ContentItem> cached = contentItemRepository.findByTargetEmotionAndType(emotion, type);

        List<ContentItem> fresh;
        if ("quote".equalsIgnoreCase(type)) {
            fresh = quoteService.generateQuotesForEmotion(emotion);
        } else if ("story".equalsIgnoreCase(type)) {
            fresh = storyService.generateStoriesForEmotion(emotion);
        } else if ("book".equalsIgnoreCase(type)) {
            fresh = bookService.generateBooksForEmotion(emotion);
        } else {
            fresh = youTubeService.searchContentForEmotion(emotion, type);
        }

        List<ContentItem> newlySaved = new ArrayList<>();
        for (ContentItem item : fresh) {
            newlySaved.add(getOrCacheExternalContent(item.getExternalId(), item.getSourceApi(), item));
        }

        Map<Long, ContentItem> merged = new LinkedHashMap<>();
        for (ContentItem item : cached) merged.put(item.getId(), item);
        for (ContentItem item : newlySaved) merged.put(item.getId(), item);

        return new ArrayList<>(merged.values());
    }

    @Override
    public ContentItem getOrCacheExternalContent(String externalId, String sourceApi, ContentItem newItem) {
        Optional<ContentItem> existing = contentItemRepository
                .findByExternalIdAndSourceApi(externalId, sourceApi);

        if (existing.isPresent()) {
            return existing.get();
        }

        return contentItemRepository.save(newItem);
    }
}