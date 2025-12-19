package com.marselgaisin.mediacms.content.podcast.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.marselgaisin.mediacms.content.podcast.dto.PodcastCreateRequest;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastEpisodeDto;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastResponse;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastUpdateRequest;
import com.marselgaisin.mediacms.content.podcast.model.Podcast;
import com.marselgaisin.mediacms.content.podcast.model.PodcastEpisode;

@Component
public class PodcastMapper {

    public Podcast toEntity(PodcastCreateRequest request) {
        Podcast podcast = new Podcast();
        podcast.setTitle(request.getTitle());
        podcast.setUrl(request.getUrl());
        return podcast;
    }

    public void updateEntity(Podcast podcast, PodcastUpdateRequest request) {
        podcast.setTitle(request.getTitle());
        podcast.setUrl(request.getUrl());
    }

    public List<PodcastEpisode> toEpisodeEntities(List<PodcastEpisodeDto> dtos, Podcast podcast) {
        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }
        List<PodcastEpisode> episodes = new ArrayList<>(dtos.size());
        for (PodcastEpisodeDto dto : dtos) {
            PodcastEpisode episode = new PodcastEpisode();
            episode.setPodcast(podcast);
            episode.setEpisodeNumber(dto.getEpisodeNumber());
            episode.setTitle(dto.getTitle());
            episode.setUrl(dto.getUrl());
            episode.setDurationSeconds(dto.getDurationSeconds());
            episodes.add(episode);
        }
        return episodes;
    }

    public PodcastResponse toResponse(Podcast podcast) {
        PodcastResponse response = new PodcastResponse();
        response.setId(podcast.getId());
        response.setTitle(podcast.getTitle());
        response.setUrl(podcast.getUrl());
        response.setCreatedAt(podcast.getCreatedAt());
        response.setUpdatedAt(podcast.getUpdatedAt());
        response.setEpisodes(toEpisodeDtos(podcast.getEpisodes()));
        return response;
    }

    private List<PodcastEpisodeDto> toEpisodeDtos(List<PodcastEpisode> episodes) {
        if (episodes == null || episodes.isEmpty()) {
            return Collections.emptyList();
        }
        List<PodcastEpisodeDto> dtos = new ArrayList<>(episodes.size());
        for (PodcastEpisode episode : episodes) {
            PodcastEpisodeDto dto = new PodcastEpisodeDto();
            dto.setId(episode.getId());
            dto.setEpisodeNumber(episode.getEpisodeNumber());
            dto.setTitle(episode.getTitle());
            dto.setUrl(episode.getUrl());
            dto.setDurationSeconds(episode.getDurationSeconds());
            dtos.add(dto);
        }
        return dtos;
    }
}
