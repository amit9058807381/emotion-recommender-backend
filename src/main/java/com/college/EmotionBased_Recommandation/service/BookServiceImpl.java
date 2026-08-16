package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import com.college.EmotionBased_Recommandation.helper.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    public BookServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public List<ContentItem> generateBooksForEmotion(String emotion) {
        String prompt = "Suggest exactly 4 real, well-known books that would help or resonate with someone feeling "
                + emotion + ". For each book, format exactly as: Title | Author | One-sentence description of why it fits this mood. "
                + "One book per line. No numbering, no extra text, no markdown.";

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