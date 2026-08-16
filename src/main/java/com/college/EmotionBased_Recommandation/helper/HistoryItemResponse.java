package com.college.EmotionBased_Recommandation.helper;

import lombok.*;

import java.time.LocalDateTime;
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HistoryItemResponse {
    private Long contentItemId;
    private String title;
    private String type;
    private String url;
    private String thumbnailUrl;
    private String emotion;
    private LocalDateTime watchedAt;



}