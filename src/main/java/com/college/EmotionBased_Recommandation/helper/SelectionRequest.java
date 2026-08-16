package com.college.EmotionBased_Recommandation.helper;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SelectionRequest {
    private Long userId;
    private Long contentItemId;
    private String emotion;


}
