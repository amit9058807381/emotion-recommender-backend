package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    private final ChatClient chatClient;

    public BookServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public List<ContentItem> generateBooksForEmotion(String emotion) {
        String prompt = "Suggest exactly 4 real, well-known books that would help or resonate with someone feeling "
                + emotion + ". For each book, format exactly as: Title | Author | One-sentence description of why it fits this mood. "
                + "One book per line. No numbering, no extra text, no markdown.";

        String rawText = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        List<ContentItem> results = new ArrayList<>();

        if (rawText == null || rawText.isBlank()) {
            return results;
        }

        String[] lines = rawText.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || !trimmed.contains("|")) continue;

            String[] parts = trimmed.split("\\|");
            if (parts.length < 3) continue;

            String title = parts[0].trim();
            String author = parts[1].trim();
            String description = parts[2].trim();

            ContentItem item = new ContentItem();
            item.setExternalId(UUID.randomUUID().toString());
            item.setSourceApi("Gemini");
            item.setTitle(title + " — " + author);
            item.setType("book");
            item.setTargetEmotion(emotion);
            item.setDescription(description);
            item.setUrl(null);
            item.setThumbnailUrl(null);
            results.add(item);
        }

        return results;
    }
}