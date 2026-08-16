package com.college.EmotionBased_Recommandation.helper;

import lombok.*;

// Matches whatever your chosen emotion API actually returns —
// you'll adjust field names once you pick Azure Face / Face++ / etc.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class EmotionApiResponse {
    private String emotion;
    private Double confidence;


    // getters/setters (or @Data if using Lombok)
}
