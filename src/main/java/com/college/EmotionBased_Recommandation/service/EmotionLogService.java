package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.EmotionLog;
import com.college.EmotionBased_Recommandation.helper.EmotionResult;

import java.util.List;

public interface EmotionLogService {
    EmotionLog saveEmotionLog(Long userId, EmotionResult result);
    List<EmotionLog> getUserEmotionHistory(Long userId);
}
