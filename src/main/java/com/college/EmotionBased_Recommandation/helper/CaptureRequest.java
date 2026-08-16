package com.college.EmotionBased_Recommandation.helper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaptureRequest {
    private Long userId;
    private String imageBase64; // frontend webcam se captured frame, base64 string


}
