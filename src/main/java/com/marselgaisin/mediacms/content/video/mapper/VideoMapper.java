package com.marselgaisin.mediacms.content.video.mapper;

import org.springframework.stereotype.Component;

import com.marselgaisin.mediacms.content.video.dto.VideoCreateRequest;
import com.marselgaisin.mediacms.content.video.dto.VideoResponse;
import com.marselgaisin.mediacms.content.video.dto.VideoUpdateRequest;
import com.marselgaisin.mediacms.content.video.model.Video;

@Component
public class VideoMapper {

    public Video toEntity(VideoCreateRequest request) {
        Video video = new Video();
        video.setTitle(request.getTitle());
        video.setUrl(request.getUrl());
        video.setDurationSeconds(request.getDurationSeconds());
        return video;
    }

    public void updateEntity(Video video, VideoUpdateRequest request) {
        video.setTitle(request.getTitle());
        video.setUrl(request.getUrl());
        video.setDurationSeconds(request.getDurationSeconds());
    }

    public VideoResponse toResponse(Video video) {
        VideoResponse response = new VideoResponse();
        response.setId(video.getId());
        response.setTitle(video.getTitle());
        response.setUrl(video.getUrl());
        response.setDurationSeconds(video.getDurationSeconds());
        response.setCreatedAt(video.getCreatedAt());
        response.setUpdatedAt(video.getUpdatedAt());
        return response;
    }
}
