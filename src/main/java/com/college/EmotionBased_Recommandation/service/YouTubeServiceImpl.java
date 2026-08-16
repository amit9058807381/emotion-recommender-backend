package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.ContentItem;
import com.college.EmotionBased_Recommandation.helper.YouTubeSearchResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class YouTubeServiceImpl implements YouTubeService {

    private final WebClient webClient;

    @Value("${youtube.api.key}")
    private String apiKey;

    private static final Map<String, String> VIDEO_QUERY_MAP = Map.of(
            "Happiness", "feel good happy music video",
            "Sadness", "motivational video for sad mood",
            "Anger", "calming music to relax anger",
            "Fear", "comforting calm relaxing video",
            "Surprise", "amazing wow interesting video",
            "Neutral", "calm focus lofi video"
    );

    private static final Map<String, String> MOVIE_QUERY_MAP = Map.of(
            "Happiness", "feel good comedy movie trailer",
            "Sadness", "uplifting inspiring movie trailer",
            "Anger", "calm peaceful movie trailer",
            "Fear", "comforting family movie trailer",
            "Surprise", "mind blowing movie trailer",
            "Neutral", "popular movie trailer"
    );

    private static final Map<String, String> MUSIC_QUERY_MAP = Map.of(
            "Happiness", "happy upbeat songs official audio",
            "Sadness", "sad emotional songs official audio",
            "Anger", "calming relaxing instrumental music",
            "Fear", "soothing calm music official audio",
            "Surprise", "energetic exciting songs official audio",
            "Neutral", "chill lofi focus music"
    );

    public YouTubeServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public List<ContentItem> searchContentForEmotion(String emotion, String type) {
        String query;
        if ("music".equalsIgnoreCase(type)) {
            query = MUSIC_QUERY_MAP.getOrDefault(emotion, "chill music");
        } else if ("movie".equalsIgnoreCase(type)) {
            query = MOVIE_QUERY_MAP.getOrDefault(emotion, "popular movie trailer");
        } else {
            query = VIDEO_QUERY_MAP.getOrDefault(emotion, "relaxing video");
        }
        return search(query, emotion, type);
    }

    private List<ContentItem> search(String query, String emotion, String type) {
        YouTubeSearchResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("www.googleapis.com")
                        .path("/youtube/v3/search")
                        .queryParam("part", "snippet")
                        .queryParam("q", query)
                        .queryParam("type", "video")
                        .queryParam("maxResults", 5)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(YouTubeSearchResponse.class)
                .block();

        List<ContentItem> results = new ArrayList<>();
        if (response == null || response.getItems() == null) return results;

        for (YouTubeSearchResponse.Item item : response.getItems()) {
            ContentItem contentItem = getContentItem(emotion, type, item);
            results.add(contentItem);
        }
        return results;
    }

    private static @NonNull ContentItem getContentItem(String emotion, String type, YouTubeSearchResponse.Item item) {
        ContentItem contentItem = new ContentItem();
        contentItem.setExternalId(item.getId().getVideoId());
        contentItem.setSourceApi("YouTube");
        contentItem.setTitle(item.getSnippet().getTitle());
        contentItem.setType(type);
        contentItem.setTargetEmotion(emotion);
        contentItem.setDescription(item.getSnippet().getDescription());
        contentItem.setUrl("https://www.youtube.com/watch?v=" + item.getId().getVideoId());
        contentItem.setThumbnailUrl(item.getSnippet().getThumbnails().getDefault().getUrl());
        return contentItem;
    }
}