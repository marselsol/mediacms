package com.marselgaisin.mediacms.content.video.service;

import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marselgaisin.mediacms.common.exception.NotFoundException;
import com.marselgaisin.mediacms.content.video.dto.VideoCreateRequest;
import com.marselgaisin.mediacms.content.video.dto.VideoResponse;
import com.marselgaisin.mediacms.content.video.dto.VideoUpdateRequest;
import com.marselgaisin.mediacms.content.video.mapper.VideoMapper;
import com.marselgaisin.mediacms.content.video.model.Video;
import com.marselgaisin.mediacms.content.video.repository.VideoRepository;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    public VideoService(VideoRepository videoRepository, VideoMapper videoMapper) {
        this.videoRepository = videoRepository;
        this.videoMapper = videoMapper;
    }

    @Transactional
    public VideoResponse create(VideoCreateRequest request) {
        Video video = videoMapper.toEntity(request);
        Video saved = videoRepository.saveAndFlush(video);
        return videoMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "videos", key = "#id")
    public VideoResponse getById(UUID id) {
        Video video = getEntity(id);
        return videoMapper.toResponse(video);
    }

    @Transactional
    @CacheEvict(cacheNames = "videos", key = "#id")
    public VideoResponse update(UUID id, VideoUpdateRequest request) {
        Video video = getEntity(id);
        videoMapper.updateEntity(video, request);
        Video saved = videoRepository.saveAndFlush(video);
        return videoMapper.toResponse(saved);
    }

    @Transactional
    @CacheEvict(cacheNames = "videos", key = "#id")
    public void delete(UUID id) {
        Video video = getEntity(id);
        videoRepository.delete(video);
    }

    private Video getEntity(UUID id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Video not found"));
    }
}
