package com.marselgaisin.mediacms.content.video.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marselgaisin.mediacms.content.video.model.Video;

public interface VideoRepository extends JpaRepository<Video, UUID> {
}
