package com.college.EmotionBased_Recommandation.helper;

public class EmotionResult {
    private final String emotion;
    private final Double confidence;

    public EmotionResult(String emotion, Double confidence) {
        this.emotion = emotion;
        this.confidence = confidence;
    }
    public String getEmotion() { return emotion; }
    public Double getConfidence() { return confidence; }
}
