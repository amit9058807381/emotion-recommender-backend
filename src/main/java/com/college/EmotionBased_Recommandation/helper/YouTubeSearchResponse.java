package com.college.EmotionBased_Recommandation.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class YouTubeSearchResponse {

    private List<Item> items;

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    @Setter
    @Getter
    public static class Item {
        private Id id;
        private Snippet snippet;

    }

    @Setter
    @Getter
    public static class Id {
        private String videoId;

    }

    @Setter
    @Getter
    public static class Snippet {
        private String title;
        private String description;
        private Thumbnails thumbnails;

    }

    public static class Thumbnails {
        @JsonProperty("default")
        private Thumbnail defaultThumbnail;

        public Thumbnail getDefault() { return defaultThumbnail; }
        public void setDefault(Thumbnail defaultThumbnail) { this.defaultThumbnail = defaultThumbnail; }
    }

    @Setter
    @Getter
    public static class Thumbnail {
        private String url;

    }
}