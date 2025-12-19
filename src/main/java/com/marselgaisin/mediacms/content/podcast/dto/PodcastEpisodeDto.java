package com.marselgaisin.mediacms.content.podcast.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

public class PodcastEpisodeDto {

    private UUID id;

    @NotNull(message = "Episode number is required")
    @Positive(message = "Episode number must be positive")
    private Integer episodeNumber;

    @NotBlank(message = "Episode title is required")
    @Size(max = 255, message = "Episode title must be at most 255 characters")
    private String title;

    @NotBlank(message = "Episode URL is required")
    @URL(message = "Episode URL must be valid")
    @Size(max = 2048, message = "Episode URL must be at most 2048 characters")
    private String url;

    @NotNull(message = "Episode duration is required")
    @Positive(message = "Episode duration must be positive")
    private Integer durationSeconds;

    public PodcastEpisodeDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
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

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}
