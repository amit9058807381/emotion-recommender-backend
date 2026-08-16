package com.college.EmotionBased_Recommandation.service;

import com.college.EmotionBased_Recommandation.entity.EmotionLog;
import com.college.EmotionBased_Recommandation.entity.User;
import com.college.EmotionBased_Recommandation.helper.EmotionResult;
import com.college.EmotionBased_Recommandation.repository.EmotionLogRepository;
import com.college.EmotionBased_Recommandation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmotionLogServiceImpl implements EmotionLogService {

    @Autowired
    private EmotionLogRepository emotionLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public EmotionLog saveEmotionLog(Long userId, EmotionResult result) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        EmotionLog log = new EmotionLog();
        log.setUser(user);
        log.setEmotion(result.getEmotion());
        log.setConfidenceScore(result.getConfidence());
        log.setDetectedAt(LocalDateTime.now());

        return emotionLogRepository.save(log);
    }

    @Override
    public List<EmotionLog> getUserEmotionHistory(Long userId) {
        return emotionLogRepository.findByUserIdOrderByDetectedAtDesc(userId);
    }
}
