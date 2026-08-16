package com.college.EmotionBased_Recommandation.repository;

import com.college.EmotionBased_Recommandation.entity.EmotionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmotionLogRepository extends JpaRepository<EmotionLog, Long> {

    List<EmotionLog> findByUserIdOrderByDetectedAtDesc(Long userId);

    List<EmotionLog> findByUserIdAndEmotion(Long userId, String emotion);
}
