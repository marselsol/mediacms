package com.marselgaisin.mediacms.content.podcast.controller;

import java.util.UUID;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
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

import com.marselgaisin.mediacms.content.podcast.dto.PodcastCreateRequest;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastResponse;
import com.marselgaisin.mediacms.content.podcast.dto.PodcastUpdateRequest;
import com.marselgaisin.mediacms.content.podcast.service.PodcastService;

@RestController
@RequestMapping("/api/podcasts")
@RequiredArgsConstructor
public class PodcastController {

    private final PodcastService podcastService;

    @PostMapping
    public ResponseEntity<PodcastResponse> create(@Valid @RequestBody PodcastCreateRequest request) {
        PodcastResponse response = podcastService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PodcastResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(podcastService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PodcastResponse> update(@PathVariable UUID id,
                                                  @Valid @RequestBody PodcastUpdateRequest request) {
        return ResponseEntity.ok(podcastService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        podcastService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
