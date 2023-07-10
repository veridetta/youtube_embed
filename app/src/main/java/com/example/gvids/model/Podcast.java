package com.example.gvids.model;

public class Podcast {
    private String podcastId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String audioUrl;

    public Podcast() {
        // Default constructor required for Firestore
    }

    public Podcast(String podcastId, String title, String description, String thumbnailUrl, String audioUrl) {
        this.podcastId = podcastId;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.audioUrl = audioUrl;
    }

    public String getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(String podcastId) {
        this.podcastId = podcastId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}

