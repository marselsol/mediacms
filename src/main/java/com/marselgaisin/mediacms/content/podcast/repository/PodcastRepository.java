package com.marselgaisin.mediacms.content.podcast.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marselgaisin.mediacms.content.podcast.model.Podcast;

public interface PodcastRepository extends JpaRepository<Podcast, UUID> {
}
