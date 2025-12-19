package com.marselgaisin.mediacms.content.video.controller;

import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marselgaisin.mediacms.content.video.dto.VideoCreateRequest;
import com.marselgaisin.mediacms.content.video.dto.VideoResponse;
import com.marselgaisin.mediacms.content.video.dto.VideoUpdateRequest;
import com.marselgaisin.mediacms.content.video.service.VideoService;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    public ResponseEntity<VideoResponse> create(@Valid @RequestBody VideoCreateRequest request) {
        VideoResponse response = videoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(videoService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoResponse> update(@PathVariable UUID id,
                                                @Valid @RequestBody VideoUpdateRequest request) {
        return ResponseEntity.ok(videoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        videoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
