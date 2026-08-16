package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.helper.EmotionResult;

public interface EmotionDetectionService {

    /**
     * Sends an image to the external Emotion Detection API
     * and returns the detected emotion with confidence score.
     */
    EmotionResult detectEmotion(byte[] imageBytes);
}