package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class StoryServiceImpl implements StoryService {

    private final ChatClient chatClient;

    public StoryServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public List<ContentItem> generateStoriesForEmotion(String emotion) {
        String prompt = "Write exactly 3 very short stories (each 4-6 sentences) suitable for someone feeling "
                + emotion + ". Each story should be comforting, relatable, or uplifting depending on the emotion. "
                + "Separate each story with the exact delimiter '###' on its own line. "
                + "For each story, start with a short title on the first line (no 'Title:' prefix, just the title text), "
                + "then a blank line, then the story text. Do not add any numbering or extra commentary.";

        String rawText = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        List<ContentItem> results = new ArrayList<>();

        if (rawText == null || rawText.isBlank()) {
            return results;
        }

        String[] stories = rawText.split("###");

        for (String storyBlock : stories) {
            String trimmed = storyBlock.trim();
            if (trimmed.isEmpty()) continue;

            String[] lines = trimmed.split("\n", 2);
            String title = lines[0].trim();
            String body = lines.length > 1 ? lines[1].trim() : trimmed;

            ContentItem item = new ContentItem();
            item.setExternalId(UUID.randomUUID().toString());
            item.setSourceApi("Gemini");
            item.setTitle(title);
            item.setType("story");
            item.setTargetEmotion(emotion);
            item.setDescription(body);
            item.setUrl(null);
            item.setThumbnailUrl(null);
            results.add(item);
        }

        return results;
    }
}