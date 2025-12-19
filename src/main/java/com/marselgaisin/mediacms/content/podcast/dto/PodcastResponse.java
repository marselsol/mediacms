package com.marselgaisin.mediacms.content.podcast.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class PodcastResponse {

    private UUID id;
    private String title;
    private String url;
    private Instant createdAt;
    private Instant updatedAt;
    private List<PodcastEpisodeDto> episodes;

    public PodcastResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<PodcastEpisodeDto> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<PodcastEpisodeDto> episodes) {
        this.episodes = episodes;
    }
}
