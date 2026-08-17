package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class QuoteServiceImpl implements QuoteService {

    private final ChatClient chatClient;

    public QuoteServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public List<ContentItem> generateQuotesForEmotion(String emotion) {
        String prompt = "Give me exactly 5 short motivational or comforting quotes for someone feeling "
                + emotion + ". Each quote should be on its own line, with no numbering, no author name, "
                + "no quotation marks, and no extra text — just the 5 quotes, one per line.";

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
            String quote = line.trim();
            if (quote.isEmpty()) continue;

            ContentItem item = new ContentItem();
            item.setExternalId(UUID.randomUUID().toString());
            item.setSourceApi("Gemini");
            item.setTitle(quote);
            item.setType("quote");
            item.setTargetEmotion(emotion);
            item.setDescription(quote);
            item.setUrl(null);
            item.setThumbnailUrl(null);
            results.add(item);
        }

        return results;
    }
}