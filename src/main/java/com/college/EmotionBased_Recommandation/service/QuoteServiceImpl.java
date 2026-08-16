package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import com.college.EmotionBased_Recommandation.helper.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;

@Service
public class QuoteServiceImpl implements QuoteService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    public QuoteServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public List<ContentItem> generateQuotesForEmotion(String emotion) {
        String prompt = "Give me exactly 5 short motivational or comforting quotes for someone feeling "
                + emotion + ". Each quote should be on its own line, with no numbering, no author name, "
                + "no quotation marks, and no extra text — just the 5 quotes, one per line.";

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
            String quote = line.trim();
            if (quote.isEmpty()) continue;

            ContentItem item = new ContentItem();
            item.setExternalId(UUID.randomUUID().toString()); // each AI quote is unique
            item.setSourceApi("Gemini");
            item.setTitle(quote);
            item.setType("quote");
            item.setTargetEmotion(emotion);
            item.setDescription(quote);
            item.setUrl(null); // quotes don't need an external link
            item.setThumbnailUrl(null);
            results.add(item);
        }

        return results;
    }
}