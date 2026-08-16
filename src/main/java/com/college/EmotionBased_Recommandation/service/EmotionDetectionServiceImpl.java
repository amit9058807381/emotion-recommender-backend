package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.helper.EmotionResult;
import com.college.EmotionBased_Recommandation.helper.FacePlusPlusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Base64;
import java.util.Map;

@Service
public class EmotionDetectionServiceImpl implements EmotionDetectionService {

    private final WebClient webClient;

    @Value("${emotion.api.url}")
    private String apiUrl;

    @Value("${emotion.api.key}")
    private String apiKey;

    @Value("${emotion.api.secret}")
    private String apiSecret;

    public EmotionDetectionServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public EmotionResult detectEmotion(byte[] imageBytes) {
        String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("api_key", apiKey);
        formData.add("api_secret", apiSecret);
        formData.add("image_base64", imageBase64);
        formData.add("return_attributes", "emotion");

        FacePlusPlusResponse response = webClient.post()
                .uri(apiUrl)
                .contentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(String.class).map(body -> {
                            System.out.println("Face++ ERROR BODY: " + body);
                            return new RuntimeException("Face++ error: " + body);
                        })
                )
                .bodyToMono(FacePlusPlusResponse.class)
                .block();

        if (response == null || response.getFaces() == null || response.getFaces().isEmpty()) {
            throw new RuntimeException("No face detected in image");
        }

        Map<String, Double> emotionScores = response.getFaces().get(0).getAttributes().getEmotion();

        String topEmotion = emotionScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("neutral");

        Double confidence = emotionScores.get(topEmotion) / 100.0;

        return new EmotionResult(capitalize(topEmotion), confidence);
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}