package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import com.college.EmotionBased_Recommandation.helper.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;

@Service
public class StoryServiceImpl implements StoryService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    public StoryServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public List<ContentItem> generateStoriesForEmotion(String emotion) {
        String prompt = "Write exactly 3 very short stories (each 4-6 sentences) suitable for someone feeling "
                + emotion + ". Each story should be comforting, relatable, or uplifting depending on the emotion. "
                + "Separate each story with the exact delimiter '###' on its own line. "
                + "For each story, start with a short title on the first line (no 'Title:' prefix, just the title text), "
                + "then a blank line, then the story text. Do not add any numbering or extra commentary.";

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        GeminiResponse response = webClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .block();

        List<ContentItem> results = new ArrayList<>();

        if (response == null || response.getCandidates() == null || response.getCandidates().isEmpty()) {
            return results;
        }

        String rawText = response.getCandidates().get(0).getContent().getParts().get(0).getText();
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