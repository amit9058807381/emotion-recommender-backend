package com.college.EmotionBased_Recommandation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "content_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id")
    private String externalId; // Spotify track ID / YouTube video ID / Google Books ID

    @Column(name = "source_api")
    private String sourceApi; // "Spotify", "YouTube", "GoogleBooks", "Quotes API"

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String type; // movie, music, book, quote, video — set by which API responded

    @Column(name = "target_emotion", nullable = false)
    private String targetEmotion;

    @Column(length = 1000)
    private String description;

    private String url; // direct link (Spotify/YouTube link, book link, etc.)

    private String thumbnailUrl; // cover art / poster / video thumbnail
}