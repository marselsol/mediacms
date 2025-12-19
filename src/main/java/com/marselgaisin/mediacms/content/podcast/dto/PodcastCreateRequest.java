package com.marselgaisin.mediacms.content.podcast.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

public class PodcastCreateRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @NotBlank(message = "URL is required")
    @URL(message = "URL must be valid")
    @Size(max = 2048, message = "URL must be at most 2048 characters")
    private String url;

    @NotNull(message = "Episodes are required")
    @Valid
    private List<PodcastEpisodeDto> episodes;

    public PodcastCreateRequest() {
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

    public List<PodcastEpisodeDto> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<PodcastEpisodeDto> episodes) {
        this.episodes = episodes;
    }
}
