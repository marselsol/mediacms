package com.marselgaisin.mediacms.content.podcast.service;

import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marselgaisin.mediacms.common.exception.NotFoundException;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastCreateRequest;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastEpisodeDto;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastResponse;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastUpdateRequest;
import com.marselgaisin.mediacms.content.podcast.mapper.PodcastMapper;
import com.marselgaisin.mediacms.content.podcast.model.Podcast;
import com.marselgaisin.mediacms.content.podcast.model.PodcastEpisode;
import com.marselgaisin.mediacms.content.podcast.repository.PodcastRepository;

@Service
public class PodcastService {

    private final PodcastRepository podcastRepository;
    private final PodcastMapper podcastMapper;

    public PodcastService(PodcastRepository podcastRepository, PodcastMapper podcastMapper) {
        this.podcastRepository = podcastRepository;
        this.podcastMapper = podcastMapper;
    }

    @Transactional
    public PodcastResponse create(PodcastCreateRequest request) {
        Podcast podcast = podcastMapper.toEntity(request);
        List<PodcastEpisode> episodes = podcastMapper.toEpisodeEntities(request.getEpisodes(), podcast);
        podcast.getEpisodes().addAll(episodes);
        Podcast saved = podcastRepository.saveAndFlush(podcast);
        return podcastMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "podcasts", key = "#id")
    public PodcastResponse getById(UUID id) {
        Podcast podcast = getEntity(id);
        return podcastMapper.toResponse(podcast);
    }

    @Transactional
    @CacheEvict(cacheNames = "podcasts", key = "#id")
    public PodcastResponse update(UUID id, PodcastUpdateRequest request) {
        Podcast podcast = getEntity(id);
        podcastMapper.updateEntity(podcast, request);
        replaceEpisodes(podcast, request.getEpisodes());
        Podcast saved = podcastRepository.saveAndFlush(podcast);
        return podcastMapper.toResponse(saved);
    }

    @Transactional
    @CacheEvict(cacheNames = "podcasts", key = "#id")
    public void delete(UUID id) {
        Podcast podcast = getEntity(id);
        podcastRepository.delete(podcast);
    }

    private Podcast getEntity(UUID id) {
        return podcastRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Podcast not found"));
    }

    private void replaceEpisodes(Podcast podcast, List<PodcastEpisodeDto> episodeDtos) {
        podcast.getEpisodes().clear();
        List<PodcastEpisode> episodes = podcastMapper.toEpisodeEntities(episodeDtos, podcast);
        podcast.getEpisodes().addAll(episodes);
    }
}
