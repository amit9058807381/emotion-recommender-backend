package com.college.EmotionBased_Recommandation.controller;

import com.college.EmotionBased_Recommandation.entity.EmotionLog;
import com.college.EmotionBased_Recommandation.helper.CaptureRequest;
import com.college.EmotionBased_Recommandation.helper.EmotionResult;
import com.college.EmotionBased_Recommandation.service.EmotionDetectionService;
import com.college.EmotionBased_Recommandation.service.EmotionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/emotion")
public class EmotionController{

    @Autowired
    private EmotionDetectionService emotionDetectionService;

    @Autowired
    private EmotionLogService emotionLogService;

    // MAIN endpoint — used by live webcam capture / live video (frontend sends base64)
    @PostMapping("/detect")
    public EmotionLog detectEmotion(@RequestBody CaptureRequest request) {
        byte[] imageBytes = java.util.Base64.getDecoder().decode(request.getImageBase64());
        EmotionResult result = emotionDetectionService.detectEmotion(imageBytes);
        return emotionLogService.saveEmotionLog(request.getUserId(), result);
    }

    // TESTING-ONLY endpoint — lets you upload a file directly in Postman, no base64 conversion needed
    @PostMapping("/detect-file")
    public EmotionLog detectEmotionFromFile(
            @RequestParam("userId") Long userId,
            @RequestParam("image") MultipartFile image) throws IOException {

        EmotionResult result = emotionDetectionService.detectEmotion(image.getBytes());
        return emotionLogService.saveEmotionLog(userId, result);
    }

    @GetMapping("/history/{userId}")
    public List<EmotionLog> getHistory(@PathVariable Long userId) {
        return emotionLogService.getUserEmotionHistory(userId);
    }
}
